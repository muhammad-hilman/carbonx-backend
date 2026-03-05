package com.ecapybara.carbonx.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecapybara.carbonx.model.issb.Product;
import com.ecapybara.carbonx.model.issb.Process;
import com.ecapybara.carbonx.model.basic.Node;
import com.ecapybara.carbonx.service.LCAService;
import com.ecapybara.carbonx.service.arango.ArangoDocumentService;
import com.ecapybara.carbonx.controller.ProductController;
import com.ecapybara.carbonx.controller.ProcessController;
import com.fasterxml.jackson.databind.ObjectMapper;


import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/lca")
public class LcaController {

    private static final Logger log = LoggerFactory.getLogger(LcaController.class);

    @Autowired
    private ArangoDocumentService documentService;

    @Autowired
    private LCAService lcaService;

    @Autowired
    private ProductController productController;

    @Autowired
    private ProcessController processController;

    @Autowired
    private ObjectMapper objectMapper;

    // Change this if your ArangoDB uses a different database
    private static final String ARANGO_DB = "default";


    // @GetMapping("/{targetCollection}/{documentKey}")
    // public Mono<?> getLCA(@PathVariable String targetCollection, @PathVariable String documentKey) {
    //     switch (targetCollection) {
    //         case "products":
    //             return documentService.getDocument(targetCollection, documentKey, null, null)
    //                 .map(rawDocument -> {
    //                     Product product = objectMapper.convertValue(rawDocument, Product.class);
    //                     product = lcaService.calculateRoughCarbonFootprint(product, "default");
    //                     productController.editProduct(product.getId(), product); // still side-effecting
    //                     return product.getDPP().getCarbonFootprint();
    //                 });

    //         case "processes":
    //             return documentService.getDocument(targetCollection, documentKey, null, null)
    //                 .map(rawMap -> objectMapper.convertValue(rawMap, Process.class))
    //                 .map(process -> {
    //                     process = lcaService.calculateRoughCarbonFootprint(process, "default");
    //                     processController.editProcess(process.getId(), process); // still side-effecting
    //                     return process.getDPP().getCarbonFootprint();
    //                 });

    //         default:
    //             return Mono.error(new RuntimeException("Invalid target collection name!"));
    //     }
    // }

    @GetMapping("/{targetCollection}/{documentKey}")
    public Mono<?> getLCA(@PathVariable String targetCollection, @PathVariable String documentKey) {
        String key = documentKey.contains("/") ? documentKey.split("/")[1] : documentKey;

        Class<? extends Node> nodeClass = switch (targetCollection) {
            case "products" -> Product.class;
            case "processes" -> Process.class;
            default -> throw new RuntimeException("Invalid target collection: " + targetCollection);
        };

        return documentService.getDocument(targetCollection, key, null, null)
            .map(raw -> (Node) objectMapper.convertValue(raw, nodeClass))
            .flatMap(node -> lcaService.calculateRoughCarbonFootprint(node, "default"))
            .flatMap(node ->
                documentService.replaceDocument(targetCollection, key, node,
                    null, null, null, null, null, null)
                    .thenReturn(node)
            )
            .map(node -> node.getDPP().getCarbonFootprint());
    }

    @GetMapping("/emission/{targetCollection}/{documentKey}")
    public Mono<?> getEmissionLCA(@PathVariable String targetCollection, @PathVariable String documentKey) {
        String key = documentKey.contains("/") ? documentKey.split("/")[1] : documentKey;

        Class<? extends Node> nodeClass = switch (targetCollection) {
            case "products" -> Product.class;
            case "processes" -> Process.class;
            default -> throw new RuntimeException("Invalid target collection: " + targetCollection);
        };

        return documentService.getDocument(targetCollection, key, null, null)
            .map(raw -> (Node) objectMapper.convertValue(raw, nodeClass))
            .flatMap(node -> lcaService.calculateEmissionInformation(node, "default"))
            .flatMap(node ->
                documentService.replaceDocument(targetCollection, key, node,
                    null, null, null, null, null, null)
                    .thenReturn(node)
            )
            .map(node -> node.getDPP().getCarbonFootprint());
    }

}