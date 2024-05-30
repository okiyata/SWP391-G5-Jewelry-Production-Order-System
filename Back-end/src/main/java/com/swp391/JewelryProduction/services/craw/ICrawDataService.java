package com.swp391.JewelryProduction.services.craw;

import com.swp391.JewelryProduction.dto.MaterialDTO;

import java.io.IOException;
import java.util.List;

public interface ICrawDataService {

    void crawData() throws IOException, InterruptedException;

    void scheduledCrawl() throws IOException, InterruptedException;

    List<MaterialDTO> getAll();

}
