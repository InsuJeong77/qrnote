package hack.jbnu.qrnote.repository;

import hack.jbnu.qrnote.domain.Memo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemoRepository extends JpaRepository<Memo, Long> {

    @Query("select m from Memo m where m.qrMemo.id = :qrId order by m.gTime desc")
    List<Memo> findVCSByQRMemoId(@Param("qrId") Long qrid);
}
