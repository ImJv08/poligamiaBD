package co.edu.unbosque.poligamia.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import co.edu.unbosque.poligamia.entity.Restriccion;

@Repository
public interface RestriccionRepository extends JpaRepository<Restriccion, Long> {


    List<Restriccion> findByParejaIdAndFechaAndActivaTrue(Long parejaId, LocalDate fecha);


    List<Restriccion> findByParejaIdAndActivaTrue(Long parejaId);
    
    List<Restriccion> findByParejaId(Long parejaId);
   

    @Query("SELECT r FROM Restriccion r WHERE r.pareja.id = :parejaId AND r.activa = true " +
           "AND r.fecha = :fecha AND r.horaInicio <= :hora AND r.horaFin >= :hora")
    List<Restriccion> findActiveRestriccionesByFechaAndHora(
            @Param("parejaId") Long parejaId,
            @Param("fecha") LocalDate fecha,
            @Param("hora") LocalTime hora);
    
}