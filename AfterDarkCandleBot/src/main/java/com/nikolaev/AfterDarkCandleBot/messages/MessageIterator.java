package com.nikolaev.AfterDarkCandleBot.messages;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

@Component
public  class MessageIterator {
    private boolean previousButton = false;
    private boolean nextButton = false;
    private ListIterator<JsonNode> iterator;

    public ListIterator<JsonNode> createIterator(ArrayNode arrayNode) {
        List<JsonNode> nodeList = new ArrayList<>();
        arrayNode.forEach(nodeList::add);
        this.iterator = nodeList.listIterator();
        return this.iterator;
    }

    public JsonNode previous() {
        previousButton = true;

        if (this.iterator == null) {
            return null;
        }

        if (this.iterator.hasPrevious()) {
            JsonNode json = this.iterator.previous();

            if (nextButton) {
                nextButton = false;

                if (!this.iterator.hasPrevious()) {
                    while (this.iterator.hasNext()) {
                        this.iterator.next();
                    }
                }

                json = this.iterator.previous();
            }

            return json;
        }

        while (this.iterator.hasNext()) {
            this.iterator.next();
        }

        return previous();
    }

    public JsonNode next() {
        nextButton = true;

        if (this.iterator == null) {
            return null;
        }

        if (this.iterator.hasNext()) {
            JsonNode json = this.iterator.next();

            if (previousButton) {
                previousButton = false;

                if (!this.iterator.hasNext()) {
                    while (this.iterator.hasPrevious()) {
                        this.iterator.previous();
                    }
                }
                json = this.iterator.next();
            }

            return json;
        }

        while (this.iterator.hasPrevious()) {
            this.iterator.previous();
        }

        return next();
    }
}
