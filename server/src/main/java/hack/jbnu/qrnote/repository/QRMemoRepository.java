package hack.jbnu.qrnote.repository;

import hack.jbnu.qrnote.domain.QRMemo;
import hack.jbnu.qrnote.domain.Team;
import hack.jbnu.qrnote.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface QRMemoRepository extends JpaRepository<QRMemo, Long> {
    QRMemo findByQrcode(String qrcode);

    @Query("select q from QRMemo q where q.authTeamId in :ids")
    List<QRMemo> findQRMemosByTeamIds(@Param("ids") List<Long> ids);

    //authTeamId = 0은 개인메모
    @Query("select q from QRMemo q where q.authTeamId = 0 and q.writer = :id")
    List<QRMemo> findPersonalMemo(@Param("id") String loginId);

    @Query("select q from QRMemo q where q.authTeamId = :id")
    List<QRMemo> findByTeamId(@Param("id") Long teamId);
}
