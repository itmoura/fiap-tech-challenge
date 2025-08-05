package com.fiap.itmoura.tech_challenge.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fiap.itmoura.tech_challenge.model.dto.MenuItemDTO;
import com.fiap.itmoura.tech_challenge.model.dto.OnCreate;
import com.fiap.itmoura.tech_challenge.model.dto.OnUpdate;
import com.fiap.itmoura.tech_challenge.service.MenuItemService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/menu-items")
@Tag(name = "Itens do Cardápio", description = "Operações relacionadas aos itens do cardápio")
public class MenuItemController {

    @Autowired
    private MenuItemService menuItemService;

    @GetMapping
    @Operation(summary = "Listar todos os itens do cardápio", description = "Retorna uma lista com todos os itens do cardápio ativos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de itens retornada com sucesso")
    })
    public ResponseEntity<List<MenuItemDTO>> findAll() {
        List<MenuItemDTO> menuItems = menuItemService.findAll();
        return ResponseEntity.ok(menuItems);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar item por ID", description = "Retorna um item específico pelo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Item encontrado"),
        @ApiResponse(responseCode = "400", description = "Item não encontrado")
    })
    public ResponseEntity<MenuItemDTO> findById(
            @Parameter(description = "ID do item") @PathVariable UUID id) {
        MenuItemDTO menuItem = menuItemService.findById(id);
        return ResponseEntity.ok(menuItem);
    }

    @GetMapping("/restaurant/{restaurantId}")
    @Operation(summary = "Buscar itens por restaurante", description = "Retorna todos os itens de um restaurante")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de itens retornada com sucesso")
    })
    public ResponseEntity<List<MenuItemDTO>> findByRestaurantId(
            @Parameter(description = "ID do restaurante") @PathVariable UUID restaurantId) {
        List<MenuItemDTO> menuItems = menuItemService.findByRestaurantId(restaurantId);
        return ResponseEntity.ok(menuItems);
    }

    @GetMapping("/restaurant/{restaurantId}/available")
    @Operation(summary = "Buscar itens disponíveis por restaurante", description = "Retorna todos os itens disponíveis de um restaurante")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de itens disponíveis retornada com sucesso")
    })
    public ResponseEntity<List<MenuItemDTO>> findAvailableByRestaurantId(
            @Parameter(description = "ID do restaurante") @PathVariable UUID restaurantId) {
        List<MenuItemDTO> menuItems = menuItemService.findAvailableByRestaurantId(restaurantId);
        return ResponseEntity.ok(menuItems);
    }

    @GetMapping("/search/category")
    @Operation(summary = "Buscar itens por categoria", description = "Retorna itens de uma categoria específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de itens retornada com sucesso")
    })
    public ResponseEntity<List<MenuItemDTO>> findByCategory(
            @Parameter(description = "Categoria do item") @RequestParam String category) {
        List<MenuItemDTO> menuItems = menuItemService.findByCategory(category);
        return ResponseEntity.ok(menuItems);
    }

    @GetMapping("/search/name")
    @Operation(summary = "Buscar itens por nome", description = "Retorna itens que contenham o nome especificado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de itens retornada com sucesso")
    })
    public ResponseEntity<List<MenuItemDTO>> findByName(
            @Parameter(description = "Nome do item") @RequestParam String name) {
        List<MenuItemDTO> menuItems = menuItemService.findByName(name);
        return ResponseEntity.ok(menuItems);
    }

    @PostMapping
    @Operation(summary = "Criar novo item do cardápio", description = "Cria um novo item do cardápio")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Item criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<MenuItemDTO> create(
            @Validated(OnCreate.class) @RequestBody MenuItemDTO menuItemDTO) {
        MenuItemDTO createdMenuItem = menuItemService.create(menuItemDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMenuItem);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar item do cardápio", description = "Atualiza um item do cardápio existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Item atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou item não encontrado")
    })
    public ResponseEntity<MenuItemDTO> update(
            @Parameter(description = "ID do item") @PathVariable UUID id,
            @Validated(OnUpdate.class) @RequestBody MenuItemDTO menuItemDTO) {
        MenuItemDTO updatedMenuItem = menuItemService.update(id, menuItemDTO);
        return ResponseEntity.ok(updatedMenuItem);
    }

    @PatchMapping("/{id}/availability")
    @Operation(summary = "Atualizar disponibilidade do item", description = "Atualiza apenas a disponibilidade de um item")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Disponibilidade atualizada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Item não encontrado")
    })
    public ResponseEntity<MenuItemDTO> updateAvailability(
            @Parameter(description = "ID do item") @PathVariable UUID id,
            @Parameter(description = "Disponibilidade") @RequestParam Boolean isAvailable) {
        MenuItemDTO updatedMenuItem = menuItemService.updateAvailability(id, isAvailable);
        return ResponseEntity.ok(updatedMenuItem);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir item do cardápio", description = "Exclui um item do cardápio (soft delete)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Item excluído com sucesso"),
        @ApiResponse(responseCode = "400", description = "Item não encontrado")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID do item") @PathVariable UUID id) {
        menuItemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
