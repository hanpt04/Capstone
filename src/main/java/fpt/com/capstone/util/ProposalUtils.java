package fpt.com.capstone.util;

import fpt.com.capstone.model.CapstoneProposal;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ProposalUtils {

    private ProposalUtils() { }

    public static Map<String, Object> buildRequestBody(CapstoneProposal proposal) {
        Map<String, Object> requestBody = new HashMap<>();
        if (proposal == null) {
            return requestBody;
        }

        String combinedDocument = buildCombinedDocument(proposal);

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("proposal_id", String.valueOf(proposal.getId()));
        metadata.put("title", proposal.getTitle() != null ? proposal.getTitle() : "");
        metadata.put("status", proposal.getStatus() != null ? proposal.getStatus().toString() : "");

        requestBody.put("ids", Collections.singletonList("proposal_" + proposal.getId()));
        requestBody.put("documents", Collections.singletonList(combinedDocument));
        requestBody.put("metadatas", Collections.singletonList(metadata));

        return requestBody;
    }

    public static String buildCombinedDocument(CapstoneProposal proposal) {
        if (proposal == null) return "";

        StringBuilder sb = new StringBuilder();

        if (proposal.getContext() != null && !proposal.getContext().isEmpty()) {
            sb.append("Context: ").append(proposal.getContext()).append("\n\n");
        }

        if (proposal.getDescription() != null && !proposal.getDescription().isEmpty()) {
            sb.append("Description: ").append(proposal.getDescription()).append("\n\n");
        }

        if (proposal.getFunc() != null && !proposal.getFunc().isEmpty()) {
            sb.append("Functional Requirements: ").append(proposal.getFunc()).append("\n\n");
        }

        if (proposal.getNonFunc() != null && !proposal.getNonFunc().isEmpty()) {
            sb.append("Non-Functional Requirements: ").append(proposal.getNonFunc());
        }

        return sb.toString().trim();
    }

    public static String extractNumericId(String chromaId) {
        if (chromaId == null || !chromaId.contains("_")) {
            return chromaId; // Trả về nguyên bản nếu null hoặc không có "_"
        }

        String[] parts = chromaId.split("_");

        // Lấy phần tử cuối cùng (phòng trường hợp ID có dạng "prefix_something_123")
        if (parts.length > 1) {
            return parts[parts.length - 1];
        }

        // Trường hợp lạ như "proposal_" (split ra 1 phần tử)
        return chromaId;
    }


    public static void copyUpdatableFields(CapstoneProposal target, CapstoneProposal source) {
        if (source.getTitle() != null) {
            target.setTitle(source.getTitle());
        }
        if (source.getContext() != null) {
            target.setContext(source.getContext());
        }
        if (source.getDescription() != null) {
            target.setDescription(source.getDescription());
        }
        if (source.getFunc() != null) {
            target.setFunc(source.getFunc());
        }
        if (source.getNonFunc() != null) {
            target.setNonFunc(source.getNonFunc());
        }
        if (source.getAttachmentUrl() != null) {
            target.setAttachmentUrl(source.getAttachmentUrl());
        }
    }
}

