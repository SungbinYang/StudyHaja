package com.studyhaja.modules.zone;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

/**
 * packageName : com.studyhaja.service.zone
 * fileName : ZoneService
 * author : rovert
 * date : 2022/03/11
 * description :
 * ===========================================================
 * DATE 			AUTHOR			 NOTE
 * -----------------------------------------------------------
 * 2022/03/11       rovert         최초 생성
 */

@Service
@Transactional
@RequiredArgsConstructor
public class ZoneService {

    private final ZoneRepository zoneRepository;

//    @PostConstruct
    public void initZoneData() throws IOException {
        if (zoneRepository.count() == 0) {
            Resource resource = new ClassPathResource("zones_kr.csv");
            List<Zone> zoneList = Files.readAllLines(resource.getFile().toPath(), StandardCharsets.UTF_8).stream().map(line -> {
                String[] split = line.split(",");
                return Zone.builder().city(split[0]).localNameOfCity(split[1]).province(split[2]).build();
            }).toList();

            zoneRepository.saveAll(zoneList);
        }
    }
}
