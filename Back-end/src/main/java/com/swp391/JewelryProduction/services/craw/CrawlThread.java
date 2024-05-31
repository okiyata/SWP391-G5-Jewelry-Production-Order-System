package com.swp391.JewelryProduction.services.craw;

import com.swp391.JewelryProduction.dto.MaterialDTO;
import com.swp391.JewelryProduction.services.connection.ConnectionPage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Setter
@Getter
@Builder
public class CrawlThread implements Runnable{
    private List<MaterialDTO> materials;
    private ConnectionPage connection;
    private String urlPage;
    private int index;

    @Override
    public void run() {
        Document connectionPage = null;
        try {
            connectionPage = connection.getConnection(this.urlPage, this.index);
            Elements materials = connectionPage.select("#gr24_spot_gold_widget-11 > table > tbody > tr");

            materials.stream().forEach(product ->{
                MaterialDTO material = MaterialDTO.builder()
                        .name(product.selectFirst("th").text())
                        .price(product.selectFirst("td").text())
                        .crawlTime(LocalDateTime.now())
                        .build();
                synchronized (materials){
                    this.materials.add(material);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
