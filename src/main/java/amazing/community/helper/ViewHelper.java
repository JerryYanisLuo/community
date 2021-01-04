package amazing.community.helper;

import amazing.community.model.User;
import lombok.Data;

import java.util.HashSet;

@Data
public class ViewHelper {

    User user;
    HashSet<Integer> view;

}
