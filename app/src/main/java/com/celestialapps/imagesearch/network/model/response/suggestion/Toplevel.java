package com.celestialapps.imagesearch.network.model.response.suggestion;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Default;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Root(name = "toplevel", strict = false)
public class Toplevel {

    @ElementList(inline = true)
    private List<CompleteSuggestion> completeSuggestions;



}
