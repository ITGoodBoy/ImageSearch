package com.celestialapps.imagesearch.network.model.response.suggestion;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Root(name = "suggestion")
public class Suggestion {

    @Attribute(name = "data")
    private String data;
}
