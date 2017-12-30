package binji.demo.data.repositories.ovd;

import org.springframework.data.jpa.repository.JpaRepository;

import binji.demo.data.entities.ovd.OvdImageEntity;

/**
 * @author jesse keane
 *
 */
public interface OvdImageRepository  extends JpaRepository<OvdImageEntity, Long> {

}
