package hack.jbnu.qrnote.repository;

import hack.jbnu.qrnote.domain.QRMemo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QRMemoRepository extends JpaRepository<QRMemo, Long> {
}
