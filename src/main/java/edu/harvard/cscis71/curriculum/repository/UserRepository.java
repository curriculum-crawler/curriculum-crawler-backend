package edu.harvard.cscis71.curriculum.repository;

import edu.harvard.cscis71.curriculum.model.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository extends HashMapRepository<User, String> {

    public UserRepository() {
        super(User.class);
    }

    @Override
    <S extends User> String getEntityId(S user) {
        return user.getUsername();
    }
}
