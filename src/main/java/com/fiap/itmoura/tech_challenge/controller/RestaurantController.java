package com.fiap.itmoura.tech_challenge.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fiap.itmoura.tech_challenge.model.dto.OnCreate;
import com.fiap.itmoura.tech_challenge.model.dto.OnUpdate;
import com.fiap.itmoura.tech_challenge.model.dto.RestaurantDTO;
import com.fiap.itmoura.tech_challenge.service.RestaurantService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/restaurants")
@Tag(name = "Restaurantes", description = "Operações relacionadas aos restaurantes")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @GetMapping
    @Operation(summary = "Listar todos os restaurantes", description = "Retorna uma lista com todos os restaurantes ativos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de restaurantes retornada com sucesso")
    })
    public ResponseEntity<List<RestaurantDTO>> findAll() {
        List<RestaurantDTO> restaurants = restaurantService.findAll();
        return ResponseEntity.ok(restaurants);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar restaurante por ID", description = "Retorna um restaurante específico pelo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Restaurante encontrado"),
        @ApiResponse(responseCode = "400", description = "Restaurante não encontrado")
    })
    public ResponseEntity<RestaurantDTO> findById(
            @Parameter(description = "ID do restaurante") @PathVariable UUID id) {
        RestaurantDTO restaurant = restaurantService.findById(id);
        return ResponseEntity.ok(restaurant);
    }

    @GetMapping("/owner/{ownerId}")
    @Operation(summary = "Buscar restaurantes por proprietário", description = "Retorna todos os restaurantes de um proprietário")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de restaurantes retornada com sucesso")
    })
    public ResponseEntity<List<RestaurantDTO>> findByOwnerId(
            @Parameter(description = "ID do proprietário") @PathVariable UUID ownerId) {
        List<RestaurantDTO> restaurants = restaurantService.findByOwnerId(ownerId);
        return ResponseEntity.ok(restaurants);
    }

    @GetMapping("/search/cuisine")
    @Operation(summary = "Buscar restaurantes por tipo de culinária", description = "Retorna restaurantes que servem um tipo específico de culinária")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de restaurantes retornada com sucesso")
    })
    public ResponseEntity<List<RestaurantDTO>> findByCuisine(
            @Parameter(description = "Tipo de culinária") @RequestParam String cuisine) {
        List<RestaurantDTO> restaurants = restaurantService.findByCuisine(cuisine);
        return ResponseEntity.ok(restaurants);
    }

    @GetMapping("/search/name")
    @Operation(summary = "Buscar restaurantes por nome", description = "Retorna restaurantes que contenham o nome especificado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de restaurantes retornada com sucesso")
    })
    public ResponseEntity<List<RestaurantDTO>> findByName(
            @Parameter(description = "Nome do restaurante") @RequestParam String name) {
        List<RestaurantDTO> restaurants = restaurantService.findByName(name);
        return ResponseEntity.ok(restaurants);
    }

    @PostMapping
    @Operation(summary = "Criar novo restaurante", description = "Cria um novo restaurante")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Restaurante criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "409", description = "CNPJ ou email já existe")
    })
    public ResponseEntity<RestaurantDTO> create(
            @Validated(OnCreate.class) @RequestBody RestaurantDTO restaurantDTO) {
        RestaurantDTO createdRestaurant = restaurantService.create(restaurantDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRestaurant);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar restaurante", description = "Atualiza um restaurante existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Restaurante atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou restaurante não encontrado"),
        @ApiResponse(responseCode = "409", description = "CNPJ ou email já existe")
    })
    public ResponseEntity<RestaurantDTO> update(
            @Parameter(description = "ID do restaurante") @PathVariable UUID id,
            @Validated(OnUpdate.class) @RequestBody RestaurantDTO restaurantDTO) {
        RestaurantDTO updatedRestaurant = restaurantService.update(id, restaurantDTO);
        return ResponseEntity.ok(updatedRestaurant);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir restaurante", description = "Exclui um restaurante (soft delete)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Restaurante excluído com sucesso"),
        @ApiResponse(responseCode = "400", description = "Restaurante não encontrado")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID do restaurante") @PathVariable UUID id) {
        restaurantService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
