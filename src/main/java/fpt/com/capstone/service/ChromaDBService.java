package fpt.com.capstone.service;

import fpt.com.capstone.model.CapstoneProposal;
import fpt.com.capstone.model.DTO.DuplicateCheckResult;
import fpt.com.capstone.repository.RatioRepository;
import fpt.com.capstone.util.ProposalUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChromaDBService {

    private final RestTemplate restTemplate;
    private final RatioRepository ratioRepository;

    // Ngưỡng khoảng cách để coi là trùng lặp 0.5 là sấp xỉ 75%


    @Value("${chromadb.proxy.url:http://localhost:5000}")
    private String chromaProxyUrl;

    @Value("${chromadb.collection.name:capstone_proposals}")
    private String collectionName;


    public boolean uploadProposal(CapstoneProposal proposal) {
        try {
            String url = chromaProxyUrl + "/collections/" + collectionName + "/add";
            Map<String, Object> requestBody = ProposalUtils.buildRequestBody(proposal);
            restTemplate.postForEntity(url, requestBody, Map.class);
        }
        catch (Exception e) {
            log.error("Error uploading proposal {}: {}", proposal != null ? proposal.getId() : null, e.getMessage());
            return false;
        }
        return true;
    }


    public DuplicateCheckResult checkDuplicate(CapstoneProposal proposal) {

          double DUPLICATE_THRESHOLD  = ratioRepository.findById(1).get().getRatio();

        String url = chromaProxyUrl + "/collections/" + collectionName + "/query";
        String queryText = ProposalUtils.buildCombinedDocument(proposal);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("query_texts", Collections.singletonList(queryText));
        requestBody.put("n_results", 2); // ⭐ Chỉ lấy 2 kết quả

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, requestBody, Map.class);
            Map<String, Object> responseBody = response.getBody();

            if (responseBody == null || !responseBody.containsKey("distances")) {
                return new DuplicateCheckResult(false, null, null);
            }

            List<List<Double>> distances = (List<List<Double>>) responseBody.get("distances");
            List<List<String>> ids = (List<List<String>>) responseBody.get("ids");

            if (distances == null || distances.isEmpty() || distances.get(0).isEmpty()) {
                return new DuplicateCheckResult(false, null, null);
            }

            // ⭐ Loop qua 2 kết quả (hoặc ít hơn nếu DB có ít data)
            for (int i = 0; i < distances.get(0).size(); i++) {
                double distance = distances.get(0).get(i);
                String chromaId = ids.get(0).get(i);
                String numericId = ProposalUtils.extractNumericId(chromaId);

                log.info("🔍 Result #{}: Proposal ID = {}, Distance = {}",
                        i + 1, numericId, distance);

                // ⭐ Bỏ qua chính nó
                if (proposal.getId() != null && numericId.equals(String.valueOf(proposal.getId()))) {
                    log.info("⏭️ Skipping self (Proposal #{})", proposal.getId());
                    continue; // Bỏ qua, check kết quả tiếp theo
                }

                // ⭐ Đây là kết quả đầu tiên KHÔNG PHẢI chính nó
                log.info("🎯 Checking duplicate with Proposal #{}", numericId);

                if (distance < DUPLICATE_THRESHOLD) {
                    log.warn("❌ DUPLICATE detected with Proposal #{} (distance: {})",
                            numericId, distance);
                    return new DuplicateCheckResult(true, distance, numericId);
                } else {
                    log.info("✅ No duplicate - distance {} >= threshold {}",
                            distance, DUPLICATE_THRESHOLD);
                    return new DuplicateCheckResult(false, distance, numericId);
                }
            }

            // ⭐ Trường hợp đặc biệt: Chỉ có 1 kết quả và đó là chính nó
            log.info("✅ No other proposals to compare - no duplicate");
            return new DuplicateCheckResult(false, null, null);

        } catch (Exception e) {
            log.error("❌ Error checking duplicate: {}", e.getMessage(), e);
            throw new RuntimeException("Không thể kiểm tra trùng lặp. Vui lòng thử lại.", e);
        }
    }


    public void deleteProposal(Integer proposalId) {
        try {
            String url = chromaProxyUrl + "/collections/" + collectionName + "/delete";

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("ids", Collections.singletonList("proposal_" + proposalId));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);

            log.info("✅ Deleted proposal {} from ChromaDB", proposalId);

        } catch (Exception e) {
            log.error("❌ Error deleting proposal {}: {}", proposalId, e.getMessage());
        }
    }

    public boolean checkConnection() {
        try {
            String url = chromaProxyUrl + "/health";
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            log.error("Cannot connect to ChromaDB proxy: {}", e.getMessage());
            return false;
        }
    }
}