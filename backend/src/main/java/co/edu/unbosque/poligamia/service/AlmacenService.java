package co.edu.unbosque.poligamia.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.poligamia.dto.AlmacenDTO;
import co.edu.unbosque.poligamia.dto.SupervisorDTO;
import co.edu.unbosque.poligamia.entity.Almacen;
import co.edu.unbosque.poligamia.entity.Supervisor;
import co.edu.unbosque.poligamia.repository.AlmacenRepository;

@Service
public class AlmacenService implements CRUDOperation<AlmacenDTO> {

    @Autowired
    private AlmacenRepository almacenRepo;


    private AlmacenDTO toDTO(Almacen almacen) {
        AlmacenDTO dto = new AlmacenDTO();
        dto.setId(almacen.getId());
        dto.setNombre(almacen.getNombre());
        dto.setDireccion(almacen.getDireccion());
        dto.setBarrio(almacen.getBarrio());
        dto.setCiudad(almacen.getCiudad());
        return dto;
    }

    private Almacen toEntity(AlmacenDTO dto) {
        Almacen almacen = new Almacen();
        almacen.setId(dto.getId());
        almacen.setNombre(dto.getNombre());
        almacen.setDireccion(dto.getDireccion());
        almacen.setBarrio(dto.getBarrio());
        almacen.setCiudad(dto.getCiudad());
        return almacen;
    }

    //  crud

    @Override
    @Transactional
    public int create(AlmacenDTO data) {
        try {
            if (data.getNombre() == null || data.getDireccion() == null ||
                data.getBarrio() == null || data.getCiudad() == null) {
                return 2; // datos inválidos
            }
            Almacen almacen = toEntity(data);
            almacenRepo.save(almacen);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 2;
        }
    }

    @Override
    public List<AlmacenDTO> getAll() {
        return almacenRepo.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<AlmacenDTO> getById(Long id) {
        return almacenRepo.findById(id).map(this::toDTO);
    }

    @Override
    @Transactional
    public int deleteById(Long id) {
        if (almacenRepo.existsById(id)) {
            almacenRepo.deleteById(id);
            return 0;
        }
        return 1;
    }

    @Override
    @Transactional
    public int updateById(Long id, AlmacenDTO newData) {
        Optional<Almacen> optional = almacenRepo.findById(id);
        if (optional.isEmpty()) {
			return 1;
		}

        Almacen existing = optional.get();
        existing.setNombre(newData.getNombre());
        existing.setDireccion(newData.getDireccion());
        existing.setBarrio(newData.getBarrio());
        existing.setCiudad(newData.getCiudad());
        almacenRepo.save(existing);
        return 0;
    }

    @Override
    public long count() {
        return almacenRepo.count();
    }

    @Override
    public boolean exist(Long id) {
        return almacenRepo.existsById(id);
    }



    public List<AlmacenDTO> filtrar(String filtro) {

		List<Almacen> almacen = almacenRepo.findAll();

		return almacen.stream()
				.filter(s -> s.getNombre().contains(filtro.toLowerCase())
						|| s.getDireccion().contains(filtro.toLowerCase())
						|| s.getCiudad().contains(filtro)
						|| s.getBarrio().toLowerCase().contains(filtro.toLowerCase()))
				.map(s -> new AlmacenDTO(s.getNombre(), s.getDireccion(), s.getBarrio(), s.getCiudad()))
				.toList();
				
	}
}
