package com.usermanagement.app.userservice.infrastructure.user.persistence;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Repository for the Users
 */
@Repository
public interface UserRepository extends MongoRepository<UserDocument, String> {


  Optional<UserDocument> findByUserIdAndDeletedContains(String userId, String userDeletionStatus);

  Page<UserDocument> findByNameContainingOrEmailContainingOrUserIdContaining(String name, String email, String userId, PageRequest pagingQuery);

  boolean existsByEmailAndDeletedContains(String email, String userDeletionStatus);

  boolean existsByUserIdAndDeletedContains(String userId, String userDeletionStatus);

  Optional<UserDocument> findByEmailAndDeletedContains(String userMail, String userDeletionStatus);

}
