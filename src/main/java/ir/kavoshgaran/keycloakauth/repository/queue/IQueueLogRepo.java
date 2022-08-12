package ir.kavoshgaran.keycloakauth.repository.queue;

import ir.kavoshgaran.keycloakauth.entity.queue.QueueLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IQueueLogRepo extends JpaRepository<QueueLog, Long> {
}
