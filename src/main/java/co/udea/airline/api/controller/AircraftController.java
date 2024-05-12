package co.udea.airline.api.controller;

import co.udea.airline.api.model.jpa.model.vehicles.Aircraft;
import co.udea.airline.api.services.vehicles.service.AircraftService;
import co.udea.airline.api.utils.common.Messages;
import co.udea.airline.api.utils.common.StandardResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "Aircraft", description = "Gestión de Aviones")
@RestController
@RequestMapping("/api/aircraft")
public class AircraftController {

    @Autowired
    private AircraftService vehicleFacade;
    private final Messages messages;

    public AircraftController(Messages messages) {

        this.messages = messages;
    }

    @PostMapping("/save")
    @Operation(summary = "Permite guardar un avion")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Aircraft.class), mediaType = MediaType.APPLICATION_JSON_VALUE)
            }, description = "El avion fue guardado exitosamente"),
            @ApiResponse(responseCode = "400", description = "La petición es inválida"),
            @ApiResponse(responseCode = "500", description = "Error interno al procesar la respuesta")})
    public ResponseEntity<StandardResponse<Aircraft>> save(@Valid @RequestBody Aircraft aircraft) {
        return ResponseEntity.ok(new StandardResponse<>(StandardResponse.StatusStandardResponse.OK,
                messages.get("aircraft.save.successful"),
                vehicleFacade.save(aircraft)));
    }

    @GetMapping("/get-all")
    @Operation(summary = "Permite consultar todos los aviones")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = List.class), mediaType = MediaType.APPLICATION_JSON_VALUE)
            }, description = "Los aviones fueron consultados exitosamente"),
            @ApiResponse(responseCode = "400", description = "La petición es inválida"),
            @ApiResponse(responseCode = "500", description = "Error interno al procesar la respuesta")})
    public ResponseEntity<StandardResponse<List<Aircraft>>> findAll() {
        return ResponseEntity.ok(new StandardResponse<>(StandardResponse.StatusStandardResponse.OK,
                messages.get("aircraft.get.all.successful"),
                vehicleFacade.findByAll()));
    }

    @GetMapping("/get-all/filter")
    @Operation(summary = "Consultar los aviones paginados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Los aviones fueron consultados exitosamente",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Page.class)))),
            @ApiResponse(responseCode = "400", description = "La petición es inválida"),
            @ApiResponse(responseCode = "500", description = "Error interno al procesar la respuesta")
    })
    public ResponseEntity<StandardResponse<Page<Aircraft>>> getWithPage(
            @Parameter(description = "Página para la cual se desean recibir los resultados (0..N)")
            @RequestParam(defaultValue = "0") Integer page,
            @Parameter(description = "Número de registros por página.")
            @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "Criterio de ordenamiento en el formato: campo(,asc|desc). "
                    + "por defecto el ordenamiento es asc. "
                    + "Se permite múltiple criterio de ordenamiento.")
            @RequestParam(defaultValue = "id,asc") String sort) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc(sort.split(",")[0])));

        return ResponseEntity.ok(new StandardResponse<>(StandardResponse.StatusStandardResponse.OK,
                messages.get("aircraft.get.all.filter.successful"),
                vehicleFacade.getWithPage(pageable)));
    }


}