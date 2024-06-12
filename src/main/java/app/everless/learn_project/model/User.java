package app.everless.learn_project.model;

import app.everless.learn_project.Annotation.TableFielId;
import lombok.Data;

@Data
public class User {
    @TableFielId("id")
    private int id;
    private String userName;
}
