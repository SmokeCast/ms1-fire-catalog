package org.idea.smokecast.fireEvent.domain;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import org.idea.smokecast.fireEvent.dto.FireEventResponseDto;
import org.idea.smokecast.fireEvent.infrastructure.FireEventRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FireEventService {
    private final FireEventRepository fireEventRepository;
    private final ModelMapper modelMapper;

    public Page<FireEventResponseDto> getAllFireEvents(PageRequest pageRequest) {
        return fireEventRepository.findAll(pageRequest)
                .map(fireEventResponseDto -> modelMapper.map(fireEventResponseDto, FireEventResponseDto.class));
    }

    public FireEventResponseDto getFireEvent(Long id) {
        var fireEvent = fireEventRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fire event with ID not found: " + id));

        return modelMapper.map(fireEvent, FireEventResponseDto.class);
    }
}