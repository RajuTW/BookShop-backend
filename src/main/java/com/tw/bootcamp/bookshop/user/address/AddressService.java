package com.tw.bootcamp.bookshop.user.address;

import com.tw.bootcamp.bookshop.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressService {
    @Autowired
    private AddressRepository addressRepository;

    public Address create(@Valid CreateAddressRequest createRequest, User user) {
        Address address = Address.create(createRequest, user);
        return addressRepository.save(address);
    }

    public List<AddressResponse> getListByUser(User user) {
        return fetchAll().stream()
                .filter(adr -> adr.getUser().getId() == user.getId())
                .map(Address::toResponse)
                .collect(Collectors.toList());
    }

    public List<Address> fetchAll(){
        return addressRepository.findAll();
    }
}
