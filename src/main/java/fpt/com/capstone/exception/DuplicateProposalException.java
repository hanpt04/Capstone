package fpt.com.capstone.exception;

import fpt.com.capstone.model.DTO.DuplicateCheckResult;
import lombok.Getter;

@Getter
public class DuplicateProposalException extends RuntimeException {

    private final DuplicateCheckResult result;

    public DuplicateProposalException(String message, DuplicateCheckResult result) {
        super(message);
        this.result = result;
    }

}