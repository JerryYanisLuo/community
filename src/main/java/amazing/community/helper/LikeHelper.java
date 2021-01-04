package amazing.community.helper;

import amazing.community.model.User;
import lombok.Data;

import java.util.HashSet;

@Data
public class LikeHelper {

    private User user;
    private HashSet<Integer> like;

}
