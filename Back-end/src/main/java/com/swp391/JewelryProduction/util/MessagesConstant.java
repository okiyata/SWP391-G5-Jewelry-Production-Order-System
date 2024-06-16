package com.swp391.JewelryProduction.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessagesConstant {
    private String customerName;
    @Builder.Default
    private String companyName = "Dung";
    @Builder.Default
    private String companyContact = "jewelryshop.business@gmail.com";
    private String title;
    private String description;
    private final RequestApprovedMessage requestApprovedMessage = new RequestApprovedMessage();
    private final RequestDeclinedMessage requestDeclinedMessage = new RequestDeclinedMessage();

    public class RequestApprovedMessage {
        public String title () {
            return title = "Your Request Has Been Approved";
        }

        public String description () {
            return description = String.format(
                    """
                    Dear %1$s,
    
                    I am pleased to inform you that your request for a custom jewelry has been approved. \
                    Our team has reviewed your submission and is pleased to confirm that your request meets the necessary \
                    criteria and requirements. As a result, we will be moving forward with the design phase. Please note \
                    that any further information or documentation required will be sent to you separately. If you have any \
                    questions or concerns, please do not hesitate to reach out to us at %2$s. Thank you \
                    for your patience and cooperation.
    
                    Best regards,
                    %3$s
                    """,
                    customerName, companyContact, companyName
            );
        }
    }

    public class RequestDeclinedMessage {
        public String title () {
            return title = "Your Request Has Been Declined";
        }

        public String description () {
            return description = String.format(
                    """
                    Dear %1$s,
                    
                    I regret to inform you that your request for a custom jewelry has been declined. \
                    After careful review, our team has determined that it does not align with our current \
                    priorities or requirements. We appreciate the effort you took to submit your request \
                    and apologize for any inconvenience this may cause. If you would like to resubmit your \
                    request or discuss alternative options, please do not hesitate to reach out to us at \
                    %2$s. We are always happy to consider alternative approaches and look forward to the \
                    possibility of working together in the future.
                    
                    Best regards,
                    %3$s
                    """,
                    customerName, companyContact, companyName
            );
        }
    }

    public MessagesConstant createRequestApprovedMessage (String customerName) {
        if (customerName == null) return null;
        return MessagesConstant.builder()
                .title(requestApprovedMessage.title())
                .description(requestApprovedMessage.description())
                .build();
    }

    public MessagesConstant createRequestDeclinedMessage (String customerName) {
        if (customerName == null) return null;
        return MessagesConstant.builder()
                .title(requestDeclinedMessage.title())
                .description(requestDeclinedMessage.description())
                .build();
    }
}
