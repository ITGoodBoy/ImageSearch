package com.celestialapps.imagesearch.network.model.response.suggestion;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Root(name = "CompleteSuggestion")
public class CompleteSuggestion {

    @Element(name = "suggestion")
    private Suggestion suggestion;

}
