package com.capsule.corp.domain.mapper;

import com.capsule.corp.infrastructure.http.controllers.client.resources.ClientDetails;
import com.capsule.corp.infrastructure.http.controllers.client.resources.request.BasicClientRequest;
import com.capsule.corp.infrastructure.http.controllers.client.resources.request.CreateClientRequest;
import com.capsule.corp.infrastructure.http.controllers.client.resources.response.ClientDetailedResponse;
import com.capsule.corp.infrastructure.http.controllers.client.resources.response.ClientSummaryResponse;
import com.capsule.corp.infrastructure.http.controllers.enums.ClientStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-25T01:54:20+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.3 (Amazon.com Inc.)"
)
@Component
public class ClientMapperImpl implements ClientMapper {

    @Override
    public ClientDetails mapClientEntity(CreateClientRequest createClientRequest, String cifNumber) {
        if ( createClientRequest == null && cifNumber == null ) {
            return null;
        }

        ClientDetails.ClientDetailsBuilder clientDetails = ClientDetails.builder();

        if ( createClientRequest != null ) {
            clientDetails.title( createClientRequest.getTitle() );
            clientDetails.gender( createClientRequest.getGender() );
            clientDetails.idNumber( createClientRequest.getIdNumber() );
            clientDetails.dateOfBirth( createClientRequest.getDateOfBirth() );
            clientDetails.firstName( createClientRequest.getFirstName() );
            clientDetails.middleName( createClientRequest.getMiddleName() );
            clientDetails.lastName( createClientRequest.getLastName() );
            clientDetails.address( createClientRequest.getAddress() );
            clientDetails.cellphoneNumber( createClientRequest.getCellphoneNumber() );
            clientDetails.email( createClientRequest.getEmail() );
            clientDetails.credit( createClientRequest.getCredit() );
            clientDetails.employmentStatus( createClientRequest.getEmploymentStatus() );
            clientDetails.sourceOfFunds( createClientRequest.getSourceOfFunds() );
            clientDetails.verifiedAnnualIncome( createClientRequest.getVerifiedAnnualIncome() );
        }
        clientDetails.cifNumber( cifNumber );
        clientDetails.clientId( UUID.randomUUID() );
        clientDetails.createdAt( LocalDateTime.now() );
        clientDetails.clientStatus( ClientStatus.ACTIVE );

        return clientDetails.build();
    }

    @Override
    public ClientDetailedResponse mapClientDetailed(ClientDetails clientDetails) {
        if ( clientDetails == null ) {
            return null;
        }

        ClientDetailedResponse.ClientDetailedResponseBuilder clientDetailedResponse = ClientDetailedResponse.builder();

        clientDetailedResponse.clientDetails( clientDetails );

        clientDetailedResponse.success( true );

        return clientDetailedResponse.build();
    }

    @Override
    public ClientSummaryResponse mapClientSummary(ClientDetails clientDetails) {
        if ( clientDetails == null ) {
            return null;
        }

        ClientSummaryResponse.ClientSummaryResponseBuilder clientSummaryResponse = ClientSummaryResponse.builder();

        clientSummaryResponse.cifNumber( clientDetails.getCifNumber() );
        clientSummaryResponse.firstName( clientDetails.getFirstName() );
        clientSummaryResponse.lastName( clientDetails.getLastName() );
        clientSummaryResponse.clientStatus( clientDetails.getClientStatus() );

        clientSummaryResponse.success( true );

        return clientSummaryResponse.build();
    }

    @Override
    public BasicClientRequest mapRemoveClientRequest(String cifNumber, String reason) {
        if ( cifNumber == null && reason == null ) {
            return null;
        }

        BasicClientRequest.BasicClientRequestBuilder basicClientRequest = BasicClientRequest.builder();

        basicClientRequest.cifNumber( cifNumber );
        basicClientRequest.reason( reason );

        return basicClientRequest.build();
    }
}
