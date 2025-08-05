package com.fiap.itmoura.tech_challenge.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fiap.itmoura.tech_challenge.exception.BadRequestException;
import com.fiap.itmoura.tech_challenge.exception.ConflictRequestException;
import com.fiap.itmoura.tech_challenge.model.dto.AddressDTO;
import com.fiap.itmoura.tech_challenge.model.dto.RestaurantDTO;
import com.fiap.itmoura.tech_challenge.model.entity.Address;
import com.fiap.itmoura.tech_challenge.model.entity.Restaurant;
import com.fiap.itmoura.tech_challenge.model.entity.Users;
import com.fiap.itmoura.tech_challenge.repository.RestaurantRepository;
import com.fiap.itmoura.tech_challenge.repository.UserRepository;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private UserRepository userRepository;

    public List<RestaurantDTO> findAll() {
        return restaurantRepository.findAllActive()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public RestaurantDTO findById(UUID id) {
        Restaurant restaurant = restaurantRepository.findByIdAndActive(id)
                .orElseThrow(() -> new BadRequestException("Restaurante não encontrado"));
        
        return convertToDTO(restaurant);
    }

    public List<RestaurantDTO> findByOwnerId(UUID ownerId) {
        return restaurantRepository.findByOwnerIdAndActive(ownerId)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<RestaurantDTO> findByCuisine(String cuisine) {
        return restaurantRepository.findByCuisineContainingIgnoreCaseAndActive(cuisine)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<RestaurantDTO> findByName(String name) {
        return restaurantRepository.findByNameContainingIgnoreCaseAndActive(name)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public RestaurantDTO create(RestaurantDTO restaurantDTO) {
        // Validações
        if (restaurantRepository.existsByCnpj(restaurantDTO.getCnpj())) {
            throw new ConflictRequestException("Já existe um restaurante com este CNPJ");
        }

        if (restaurantRepository.existsByEmail(restaurantDTO.getEmail())) {
            throw new ConflictRequestException("Já existe um restaurante com este email");
        }

        // Verifica se o proprietário existe
        Users owner = userRepository.findById(restaurantDTO.getOwnerId())
                .orElseThrow(() -> new BadRequestException("Proprietário não encontrado"));

        // Converte endereço
        Address address = null;
        if (restaurantDTO.getAddress() != null) {
            address = convertToAddressEntity(restaurantDTO.getAddress());
        }

        Restaurant restaurant = Restaurant.builder()
                .name(restaurantDTO.getName())
                .description(restaurantDTO.getDescription())
                .cuisine(restaurantDTO.getCuisine())
                .cnpj(restaurantDTO.getCnpj())
                .phone(restaurantDTO.getPhone())
                .email(restaurantDTO.getEmail())
                .openingTime(restaurantDTO.getOpeningTime())
                .closingTime(restaurantDTO.getClosingTime())
                .address(address)
                .owner(owner)
                .isActive(true)
                .build();

        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        return convertToDTO(savedRestaurant);
    }

    public RestaurantDTO update(UUID id, RestaurantDTO restaurantDTO) {
        Restaurant existingRestaurant = restaurantRepository.findByIdAndActive(id)
                .orElseThrow(() -> new BadRequestException("Restaurante não encontrado"));

        // Verifica se CNPJ já existe em outro registro
        if (!existingRestaurant.getCnpj().equals(restaurantDTO.getCnpj()) && 
            restaurantRepository.existsByCnpj(restaurantDTO.getCnpj())) {
            throw new ConflictRequestException("Já existe um restaurante com este CNPJ");
        }

        // Verifica se email já existe em outro registro
        if (!existingRestaurant.getEmail().equals(restaurantDTO.getEmail()) && 
            restaurantRepository.existsByEmail(restaurantDTO.getEmail())) {
            throw new ConflictRequestException("Já existe um restaurante com este email");
        }

        // Atualiza os campos
        existingRestaurant.setName(restaurantDTO.getName());
        existingRestaurant.setDescription(restaurantDTO.getDescription());
        existingRestaurant.setCuisine(restaurantDTO.getCuisine());
        existingRestaurant.setCnpj(restaurantDTO.getCnpj());
        existingRestaurant.setPhone(restaurantDTO.getPhone());
        existingRestaurant.setEmail(restaurantDTO.getEmail());
        existingRestaurant.setOpeningTime(restaurantDTO.getOpeningTime());
        existingRestaurant.setClosingTime(restaurantDTO.getClosingTime());

        // Atualiza endereço se fornecido
        if (restaurantDTO.getAddress() != null) {
            if (existingRestaurant.getAddress() != null) {
                updateAddress(existingRestaurant.getAddress(), restaurantDTO.getAddress());
            } else {
                existingRestaurant.setAddress(convertToAddressEntity(restaurantDTO.getAddress()));
            }
        }

        Restaurant updatedRestaurant = restaurantRepository.save(existingRestaurant);
        return convertToDTO(updatedRestaurant);
    }

    public void delete(UUID id) {
        Restaurant restaurant = restaurantRepository.findByIdAndActive(id)
                .orElseThrow(() -> new BadRequestException("Restaurante não encontrado"));

        restaurant.setIsActive(false);
        restaurantRepository.save(restaurant);
    }

    private RestaurantDTO convertToDTO(Restaurant restaurant) {
        AddressDTO addressDTO = null;
        if (restaurant.getAddress() != null) {
            addressDTO = convertToAddressDTO(restaurant.getAddress());
        }

        return RestaurantDTO.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .description(restaurant.getDescription())
                .cuisine(restaurant.getCuisine())
                .cnpj(restaurant.getCnpj())
                .phone(restaurant.getPhone())
                .email(restaurant.getEmail())
                .openingTime(restaurant.getOpeningTime())
                .closingTime(restaurant.getClosingTime())
                .address(addressDTO)
                .ownerId(restaurant.getOwner().getId())
                .createdAt(restaurant.getCreatedAt())
                .updatedAt(restaurant.getUpdatedAt())
                .isActive(restaurant.getIsActive())
                .build();
    }

    private Address convertToAddressEntity(AddressDTO addressDTO) {
        return Address.builder()
                .street(addressDTO.getStreet())
                .number(addressDTO.getNumber())
                .complement(addressDTO.getComplement())
                .neighborhood(addressDTO.getNeighborhood())
                .city(addressDTO.getCity())
                .state(addressDTO.getState())
                .zipCode(addressDTO.getZipCode())
                .build();
    }

    private AddressDTO convertToAddressDTO(Address address) {
        return AddressDTO.builder()
                .id(address.getId())
                .street(address.getStreet())
                .number(address.getNumber())
                .complement(address.getComplement())
                .neighborhood(address.getNeighborhood())
                .city(address.getCity())
                .state(address.getState())
                .zipCode(address.getZipCode())
                .build();
    }

    private void updateAddress(Address existingAddress, AddressDTO addressDTO) {
        existingAddress.setStreet(addressDTO.getStreet());
        existingAddress.setNumber(addressDTO.getNumber());
        existingAddress.setComplement(addressDTO.getComplement());
        existingAddress.setNeighborhood(addressDTO.getNeighborhood());
        existingAddress.setCity(addressDTO.getCity());
        existingAddress.setState(addressDTO.getState());
        existingAddress.setZipCode(addressDTO.getZipCode());
    }
}
