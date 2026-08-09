package co.edu.unbosque.poligamia.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.poligamia.dto.RestriccionDTO;
import co.edu.unbosque.poligamia.entity.Pareja;
import co.edu.unbosque.poligamia.entity.Restriccion;
import co.edu.unbosque.poligamia.repository.ParejaRepository;
import co.edu.unbosque.poligamia.repository.RestriccionRepository;

@Service
public class RestriccionService {

	@Autowired
	private RestriccionRepository restriccionRepo;

	@Autowired
	private ParejaRepository parejaRepo;

	private RestriccionDTO toDTO(Restriccion restriccion) {
		return new RestriccionDTO(restriccion.getId(), restriccion.getPareja().getId(), restriccion.getFecha(),
				restriccion.getHoraInicio(), restriccion.getHoraFin(), restriccion.isActiva(), restriccion.getMotivo());
	}

	private Restriccion toEntity(RestriccionDTO dto) {
		Restriccion restriccion = new Restriccion();
		restriccion.setId(dto.getId());
		restriccion.setFecha(dto.getFecha());
		restriccion.setHoraInicio(dto.getHoraInicio());
		restriccion.setHoraFin(dto.getHoraFin());
		restriccion.setActiva(dto.isActiva());
		restriccion.setMotivo(dto.getMotivo());

		return restriccion;
	}

	// CRUD

	@Transactional
	public int create(RestriccionDTO dto) {
		try {

			Pareja pareja = parejaRepo.findById(dto.getParejaId())
					.orElseThrow(() -> new RuntimeException("Pareja no encontrada"));

			if (dto.getHoraInicio().isAfter(dto.getHoraFin())) {
				return 2;
			}

			List<Restriccion> existentes = restriccionRepo.findByParejaIdAndFechaAndActivaTrue(dto.getParejaId(),
					dto.getFecha());
			for (Restriccion r : existentes) {
				if (horariosSolapados(r.getHoraInicio(), r.getHoraFin(), dto.getHoraInicio(), dto.getHoraFin())) {
					return 3;
				}
			}

			Restriccion restriccion = toEntity(dto);
			restriccion.setPareja(pareja);
			restriccion.setActiva(true);
			restriccionRepo.save(restriccion);
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return 1;
		}
	}

	public List<RestriccionDTO> getAll() {
		return restriccionRepo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
	}

	public Optional<RestriccionDTO> getById(Long id) {
		return restriccionRepo.findById(id).map(this::toDTO);
	}

	public List<RestriccionDTO> getByParejaId(Long parejaId) {
		return restriccionRepo.findByParejaIdAndActivaTrue(parejaId).stream().map(this::toDTO)
				.collect(Collectors.toList());
	}

	@Transactional
	public int deleteById(Long id) {
		if (restriccionRepo.existsById(id)) {
			restriccionRepo.deleteById(id);
			return 0;
		}
		return 1;
	}

	@Transactional
	public int updateById(Long id, RestriccionDTO newData) {
		Optional<Restriccion> optional = restriccionRepo.findById(id);
		if (optional.isEmpty()) {
			return 1;
		}

		Restriccion existing = optional.get();

		existing.setFecha(newData.getFecha());
		existing.setHoraInicio(newData.getHoraInicio());
		existing.setHoraFin(newData.getHoraFin());
		existing.setActiva(newData.isActiva());
		existing.setMotivo(newData.getMotivo());

		List<Restriccion> conflictos = restriccionRepo.findByParejaIdAndFechaAndActivaTrue(existing.getPareja().getId(),
				newData.getFecha());
		for (Restriccion r : conflictos) {
			if (!r.getId().equals(id) && horariosSolapados(r.getHoraInicio(), r.getHoraFin(), newData.getHoraInicio(),
					newData.getHoraFin())) {
				return 3;
			}
		}

		restriccionRepo.save(existing);
		return 0;
	}

	public boolean tieneRestriccionActiva(Long parejaId, LocalDate fecha, LocalTime hora) {
		List<Restriccion> restricciones = restriccionRepo.findActiveRestriccionesByFechaAndHora(parejaId, fecha, hora);
		return !restricciones.isEmpty();
	}

	private boolean horariosSolapados(LocalTime inicio1, LocalTime fin1, LocalTime inicio2, LocalTime fin2) {
		return !(fin1.isBefore(inicio2) || fin2.isBefore(inicio1));
	}
	
	public long contarActivas() {
	    return restriccionRepo.findAll().stream()
	            .filter(Restriccion::isActiva)
	            .count();
	}
	
	public List<RestriccionDTO> obtenerPorPareja(Long idPareja) {

	    return restriccionRepo.findByParejaId(idPareja)
	            .stream()
	            .map(this::toDTO)
	            .toList();
	}
}