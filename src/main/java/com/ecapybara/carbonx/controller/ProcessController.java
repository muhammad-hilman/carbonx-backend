package com.ecapybara.carbonx.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.ecapybara.carbonx.config.AppLogger;
import com.ecapybara.carbonx.model.issb.Process;
import com.ecapybara.carbonx.model.issb.Product;
import com.ecapybara.carbonx.service.arango.ArangoDatabaseService;
import com.ecapybara.carbonx.service.arango.ArangoDocumentService;
import com.ecapybara.carbonx.service.arango.ArangoGraphService;
import com.ecapybara.carbonx.service.arango.ArangoQueryService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ecapybara.carbonx.service.GraphService;

import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/api/processes")
public class ProcessController {

    @Autowired
    private ArangoDocumentService documentService;
    @Autowired
    private ArangoGraphService graphService;
    @Autowired
    private ArangoQueryService queryService;
    @Autowired
    private ObjectMapper objectMapper;

    private static final Logger log = LoggerFactory.getLogger(AppLogger.class);
    final Sort sort = Sort.by(Direction.DESC, "id");
    private static final String ARANGO_DB = "default";
    private static final String ARANGO_GRAPH = "default";
    private static final String COLLECTION = "processes";

    @GetMapping
    public List<Process> searchAllProcesses(
        @RequestParam(required = false, defaultValue = ARANGO_DB) String targetDatabase) {

        String aql = "FOR p IN " + COLLECTION + " RETURN p";
        Map<String, Object> response = queryService.executeQuery(targetDatabase, aql, null, null, null, null, null).block();

        if (response == null || !response.containsKey("result")) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No processes found in database: " + targetDatabase);
        }

        return objectMapper.convertValue(response.get("result"), new TypeReference<List<Process>>() {});
    }

    @GetMapping("/search")
    public List<Process> searchProcesses(
        @RequestParam Map<String, String> filters,
        @RequestParam(required = false, defaultValue = ARANGO_DB) String targetDatabase) {

        filters.remove("targetDatabase");

        if (filters.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "At least one search criteria is required");
        }

        StringBuilder aql = new StringBuilder("FOR p IN " + COLLECTION + " FILTER ");
        Map<String, String> bindVars = new HashMap<>();
        List<String> conditions = new ArrayList<>();

        for (Map.Entry<String, String> entry : filters.entrySet()) {
            String field = entry.getKey();
            conditions.add("p." + field + " == @" + field);
            bindVars.put(field, entry.getValue());
        }

        aql.append(String.join(" AND ", conditions)).append(" RETURN p");

        Map<String, Object> response = queryService.executeQuery(targetDatabase, aql.toString(), bindVars, null, null, null, null).block();

        if (response == null || !response.containsKey("result")) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No processes found");
        }

        return objectMapper.convertValue(response.get("result"), new TypeReference<List<Process>>() {});
    }

    @GetMapping("/{key}")
    public Process getProcess(
        @PathVariable String key,
        @RequestParam(required = false, defaultValue = ARANGO_DB) String targetDatabase) {

        Map response = documentService.getDocument(COLLECTION, key, null, null).block();

        if (response == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Process not found: " + key);
        }

        return objectMapper.convertValue(response, Process.class);
    }

    @GetMapping("/{key}/field")
    public Map<String, Object> getProcessFields(
        @PathVariable String key,
        @RequestParam List<String> fieldName,
        @RequestParam(required = false, defaultValue = ARANGO_DB) String targetDatabase) {

        String fields = fieldName.stream()
            .map(f -> "\"" + f + "\": p." + f)
            .collect(Collectors.joining(", "));

        String aql = "FOR p IN " + COLLECTION + " FILTER p._key == @key RETURN {" + fields + "}";
        Map<String, String> bindVars = new HashMap<>();
        bindVars.put("key", key);

        Map<String, Object> response = queryService.executeQuery(targetDatabase, aql, bindVars, null, null, null, null).block();

        if (response == null || !response.containsKey("result")) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Process or field not found");
        }

        List<Map<String, Object>> result = (List<Map<String, Object>>) response.get("result");
        return result.isEmpty() ? null : result.get(0);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public List<Process> createProcesses(
        @RequestBody List<Process> processesList,
        @RequestParam(required = false, defaultValue = ARANGO_DB) String targetDatabase) {

        List<Object> documents = processesList.stream()
            .map(process -> {
                Map<String, Object> doc = objectMapper.convertValue(process, new TypeReference<Map<String, Object>>() {});
                if (doc.get("_key") == null) doc.remove("_key");
                return (Object) doc;
            })
            .collect(Collectors.toList());

        List<Map> response = (List<Map>) documentService.createDocuments(targetDatabase, COLLECTION, documents, null, true, null, null, null).block();

        if (response == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create processes");
        }

        return response.stream()
            .map(item -> objectMapper.convertValue(item.get("new"), Process.class))
            .collect(Collectors.toList());
    }

    @PutMapping
    public List<Process> editProcesses(
        @RequestBody List<Map<String, Object>> revisedProcesses,
        @RequestParam(required = false, defaultValue = ARANGO_DB) String targetDatabase) {

        List<Object> documents = revisedProcesses.stream()
            .map(doc -> {
                if (doc.get("_key") == null) doc.remove("_key");
                return (Object) doc;
            })
            .collect(Collectors.toList());

        List<Map> response = (List<Map>) documentService.updateDocuments(COLLECTION, documents, null, true, null, null, null, null).block();

        if (response == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update processes");
        }

        return response.stream()
            .map(item -> objectMapper.convertValue(item.get("new"), Process.class))
            .collect(Collectors.toList());
    }

    @PutMapping("/{key}")
    public Process editProcess(
        @PathVariable String key,
        @RequestBody Map<String, Object> doc,
        @RequestParam(required = false, defaultValue = ARANGO_DB) String targetDatabase) {

        doc.remove("_key");

        Map response = documentService.updateDocument(COLLECTION, key, doc, null, true, null, null, null, null, null, null).block();

        if (response == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Process not found: " + key);
        }

        return objectMapper.convertValue(response.get("new"), Process.class);
    }

    @DeleteMapping("/{key}")
    public Map<String, Object> deleteProcess(
        @PathVariable String key,
        @RequestParam(required = false, defaultValue = ARANGO_GRAPH) String graphName,
        @RequestParam(required = false, defaultValue = ARANGO_DB) String targetDatabase) {

        Map deleted = graphService.deleteVertex(targetDatabase, graphName, COLLECTION, key, null, true).block();

        if (deleted == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Process not found: " + key);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("message", "Process successfully deleted");
        result.put("deleted", objectMapper.convertValue(deleted.get("old"), Process.class));
        return result;
    }
}