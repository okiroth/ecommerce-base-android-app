package com.mevoyalsuper.userapp.models;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;

/**
 * Created by ivan on 3/20/15.
 */
public class User extends SugarRecord<User> {

    public String email;
    public String phone;
    public String token;
    public String name;

    public User() {
    }

    public static User findByEmail(String value){
        List<User> list = Select.from(User.class)
                .where(Condition.prop("email").eq(value))
                .list();

        if (list.size() != 1) {
            return null;
        } else {
            return list.get(0);
        }
    }

    public static User getLoggedUser() {
        List<User> users = User.listAll(User.class);
        if(users.size() > 0) return users.get(0);

        return null;
    }
}

