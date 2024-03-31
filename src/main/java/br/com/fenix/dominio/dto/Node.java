package br.com.fenix.dominio.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Node {

    private String nodeId; // node id
    private String pid; // parent id
    private String text;
    private String href;

    public Node(String nodeId, String pId, String text, String href) {
        this.nodeId = nodeId;
        this.pid = pId;
        this.text = text;
        this.href = href;
    }
}
