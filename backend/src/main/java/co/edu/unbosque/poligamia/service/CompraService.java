package co.edu.unbosque.poligamia.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.unbosque.poligamia.dto.CompraDTO;
import co.edu.unbosque.poligamia.entity.Almacen;
import co.edu.unbosque.poligamia.entity.Cliente;
import co.edu.unbosque.poligamia.entity.Compra;
import co.edu.unbosque.poligamia.entity.EstadoCompra;
import co.edu.unbosque.poligamia.entity.Pareja;
import co.edu.unbosque.poligamia.entity.SobreCupo;
import co.edu.unbosque.poligamia.repository.AlmacenRepository;
import co.edu.unbosque.poligamia.repository.CompraRepository;
import co.edu.unbosque.poligamia.repository.ParejaRepository;
import co.edu.unbosque.poligamia.repository.SobrecupoRepository;

@Service
public class CompraService {

	@Autowired
	private CompraRepository compraRepo;

	@Autowired
	private ParejaRepository parejaRepo;

	@Autowired
	private SobrecupoRepository sobrecupoRepo;

	@Autowired
	private AlmacenRepository almacenRepo;

	@Autowired
	private RestriccionService restriccionService;

	public int registrarCompra(Long idPareja, Long idAlmacen, double montoTransaccion) {

		// cambio
		LocalDate fechaActual = LocalDate.now();
		LocalTime horaActual = LocalTime.now();

		Optional<Pareja> found = parejaRepo.findById(idPareja);

		if (found.isEmpty()) {
			return 2;
		}

		Pareja pareja = found.get();

		// cambio
		if (restriccionService.tieneRestriccionActiva(idPareja, fechaActual, horaActual)) {
			return 4;
		}

		System.out.println(pareja.getCliente());

		Cliente cliente = pareja.getCliente();
		System.out.println(cliente);
		System.out.println(cliente.getId());

		Optional<Almacen> foundAlmacen = almacenRepo.findById(idAlmacen);
		if (foundAlmacen.isEmpty()) {
			return 3;
		}
		Almacen almacen = foundAlmacen.get();

		List<Compra> compras = compraRepo.findByParejaId(idPareja);

//		Double gastado = compras.stream().filter(c -> c.getEstado() == EstadoCompra.APROBADO)
//				.mapToDouble(Compra::getMontoTransaccion).sum();

//		Double disponible = pareja.getCupoAsignado() - gastado;

		Compra compra = new Compra();

		compra.setMontoTransaccion(montoTransaccion);
		compra.setFecha(fechaActual);
		compra.setHora(horaActual);
		compra.setPareja(pareja);
		compra.setAlmacen(almacen);

		if (montoTransaccion <= pareja.getCupoAsignado()) {
			compra.setEstado(EstadoCompra.APROBADO);
			pareja.setCupoAsignado(pareja.getCupoAsignado() - montoTransaccion);
			parejaRepo.save(pareja);
			compraRepo.save(compra);
			return 0;
		}

		compra.setEstado(EstadoCompra.PENDIENTE);
		compraRepo.save(compra);

		SobreCupo sobrecupo = new SobreCupo();

		sobrecupo.setCompra(compra);
		sobrecupo.setMontoAutorizado(montoTransaccion - pareja.getCupoAsignado());
		sobrecupo.setClienteTitular(cliente);
		sobrecupo.setAprobadoSupervisor(null);
		sobrecupo.setAutorizadoCliente(null);

		sobrecupoRepo.save(sobrecupo);

		return 1;

	}

	public int actualizarCompra(Long idCompra, CompraDTO data) {
		
		System.out.println("=== ENTRÓ AL MÉTODO ===");
	    System.out.println("data completo: " + data);
	    System.out.println("data.getIdPareja(): " + data.getIdPareja());
	    System.out.println("data.getIdAlmacen(): " + data.getIdAlmacen());

		Optional<Compra> foundCompra = compraRepo.findById(idCompra);

		if (foundCompra.isEmpty()) {
			return 2; // Compra no encontrada
		}

		Compra compra = foundCompra.get();
		
		System.out.println("idCompra = " + idCompra);
		System.out.println("idPareja = " + data.getIdPareja());
		System.out.println("idAlmacen = " + data.getIdAlmacen());

		Optional<Pareja> foundPareja = parejaRepo.findById(data.getIdPareja());

		if (foundPareja.isEmpty()) {
			return 3; // Pareja no encontrada
		}

		Pareja pareja = foundPareja.get();

		Optional<Almacen> foundAlmacen = almacenRepo.findById(data.getIdAlmacen());

		if (foundAlmacen.isEmpty()) {
			return 4; // Almacén no encontrado
		}

		Almacen almacen = foundAlmacen.get();

		if (compra.getEstado() == EstadoCompra.APROBADO) {
			pareja.setCupoAsignado(pareja.getCupoAsignado() + compra.getMontoTransaccion());
		}

		compra.setPareja(pareja);
		compra.setAlmacen(almacen);
		compra.setMontoTransaccion(data.getMontoTransaccion());

		if (data.getFecha() != null) {
			compra.setFecha(data.getFecha());
		}

		if (data.getHora() != null) {
			compra.setHora(data.getHora());
		}

		if (data.getEstado() != null) {
			if (data.getEstado() == "RECHAZADO") {
				compra.setEstado(EstadoCompra.RECHAZADO);
			}

			if (data.getEstado() == "PENDIENTE") {
				compra.setEstado(EstadoCompra.PENDIENTE);
			}

			if (data.getEstado() == "APROBADO") {
				compra.setEstado(EstadoCompra.APROBADO);
				pareja.setCupoAsignado(pareja.getCupoAsignado() - data.getMontoTransaccion());
			}
		}

		parejaRepo.save(pareja);
		compraRepo.save(compra);

		return 0;
	}

	public int actualizarEstadoCompra(Long idCompra, Long idSobrecupo, Long idPareja) {

		Optional<Compra> found = compraRepo.findById(idCompra);

		if (found.isEmpty()) {
			return 2;
		}

		Compra compra = found.get();

		Optional<Pareja> foundPareja = parejaRepo.findById(idPareja);

		if (foundPareja.isEmpty()) {
			return 2;
		}

		Pareja pareja = foundPareja.get();

		Optional<SobreCupo> foundSobreCupo = sobrecupoRepo.findById(idSobrecupo);

		if (foundSobreCupo.isEmpty()) {
			return 2;
		}
		SobreCupo sobrecupo = foundSobreCupo.get();

		if (sobrecupo.getAutorizadoCliente() == null || sobrecupo.getAprobadoSupervisor() == null) {
			return 1;
		}
		if (!sobrecupo.getCompra().getId().equals(compra.getId())) {
			return 3;
		}

		if (!sobrecupo.getAprobadoSupervisor()
				|| !sobrecupo.getAutorizadoCliente() && sobrecupo.getAutorizadoCliente() != null) {
			compra.setEstado(EstadoCompra.RECHAZADO);
		} else {
			compra.setEstado(EstadoCompra.APROBADO);

		}

		compraRepo.save(compra);
		return 0;

	}

	public List<CompraDTO> getAll() {

		List<Compra> compras = compraRepo.findAll();
		List<CompraDTO> resultado = new ArrayList<>();

		for (Compra c : compras) {

			CompraDTO dto = new CompraDTO();

			dto.setId(c.getId());
			dto.setIdPareja(c.getPareja().getId());
			dto.setIdAlmacen(c.getAlmacen().getId());
			dto.setFecha(c.getFecha());
			dto.setMontoTransaccion(c.getMontoTransaccion());
			dto.setEstado(c.getEstado().name());
			resultado.add(dto);
		}

		return resultado;
	}

	public List<CompraDTO> filtrar(String cliente, String pareja, String almacen, LocalDate fechaDesde,
			LocalDate fechaHasta, String estado) {

		List<Compra> compras = compraRepo.findAll();
		List<CompraDTO> resultado = new ArrayList<>();

		for (Compra c : compras) {

			boolean cumple = true;

			if (cliente != null && !c.getPareja().getCliente().getPrimerNombre().equalsIgnoreCase(cliente)) {
				cumple = false;
			}

			if (pareja != null && !c.getPareja().getPrimerNombre().equalsIgnoreCase(pareja)) {
				cumple = false;
			}

			if (almacen != null && !c.getAlmacen().getNombre().equalsIgnoreCase(almacen)) {
				cumple = false;
			}

			if (fechaDesde != null && c.getFecha().isBefore(fechaDesde)) {
				cumple = false;
			}

			if (fechaHasta != null && c.getFecha().isAfter(fechaHasta)) {
				cumple = false;
			}

			if (estado != null && !c.getEstado().name().equalsIgnoreCase(estado)) {
				cumple = false;
			}

			if (cumple) {
				CompraDTO dto = new CompraDTO();
				dto.setId(c.getId());
				dto.setIdPareja(c.getPareja().getId());
				dto.setIdAlmacen(c.getAlmacen().getId());
				dto.setFecha(c.getFecha());
				dto.setHora(c.getHora());
				dto.setMontoTransaccion(c.getMontoTransaccion());
				dto.setEstado(c.getEstado().name());
				resultado.add(dto);
			}
		}

		return resultado;
	}

	public long contarComprasHoy() {
		LocalDate hoy = LocalDate.now();

		return compraRepo.findAll().stream().filter(c -> c.getFecha().isEqual(hoy)).count();
	}

	public List<CompraDTO> obtenerComprasCliente(Long idCliente) {
		List<Compra> compras = compraRepo.findByParejaClienteId(idCliente);
		List<CompraDTO> resultado = new ArrayList<>();

		for (Compra c : compras) {

			CompraDTO dto = new CompraDTO();

			dto.setId(c.getId());
			dto.setIdPareja(c.getPareja().getId());
			dto.setIdAlmacen(c.getAlmacen().getId());
			dto.setFecha(c.getFecha());
			dto.setMontoTransaccion(c.getMontoTransaccion());
			dto.setEstado(c.getEstado().name());
			resultado.add(dto);
		}

		return resultado;

	}

	public List<CompraDTO> obtenerComprasPareja(Long idPareja) {
		List<Compra> compras = compraRepo.findByParejaId(idPareja);
		List<CompraDTO> resultado = new ArrayList<>();

		for (Compra c : compras) {

			CompraDTO dto = new CompraDTO();

			dto.setId(c.getId());
			dto.setIdPareja(c.getPareja().getId());
			dto.setIdAlmacen(c.getAlmacen().getId());
			dto.setFecha(c.getFecha());
			dto.setMontoTransaccion(c.getMontoTransaccion());
			dto.setEstado(c.getEstado().name());
			resultado.add(dto);
		}

		return resultado;
	}
	
	public int eliminarCompra(Long idCompra) {

	    Optional<Compra> found = compraRepo.findById(idCompra);

	    if (found.isEmpty()) {
	        return 1;
	    }

	    Compra compra = found.get();

	    // Si existe un sobrecupo asociado, eliminarlo primero
	    SobreCupo sobrecupo = sobrecupoRepo.findByCompraId(compra.getId());

	    if (sobrecupo != null) {
	        sobrecupoRepo.delete(sobrecupo);
	    }

	    compraRepo.delete(compra);

	    return 0;
	}

}