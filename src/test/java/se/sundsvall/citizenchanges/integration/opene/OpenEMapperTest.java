package se.sundsvall.citizenchanges.integration.opene;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import se.sundsvall.citizenchanges.api.model.FamilyType;
import se.sundsvall.citizenchanges.integration.opene.util.OpenEMapper;

@ExtendWith(MockitoExtension.class)
class OpenEMapperTest {

	@InjectMocks
	private OpenEMapper mapper;

	@Test
	void mapFlowIds() {
		final var inputStream = IOUtils.toInputStream("""
			<FlowInstances>
				<FlowInstance>
					<flowInstanceID>172698</flowInstanceID>
					<added>2024-03-17 17:38</added>
					<updated>2024-04-17 14:41</updated>
					<firstSubmitted>2024-03-17 17:38</firstSubmitted>
				</FlowInstance>
				<FlowInstance>
					<flowInstanceID>172529</flowInstanceID>
					<added>2024-03-15 14:40</added>
					<updated>2024-03-18 09:52</updated>
					<remote>false</remote>
					<addedInMilliseconds>1710510056000</addedInMilliseconds>
					<updatedInMilliseconds>1710751974000</updatedInMilliseconds>
					<firstSubmittedInMilliseconds>1710510298000</firstSubmittedInMilliseconds>
					<lastStatusChangeInMilliseconds>1710751998000</lastStatusChangeInMilliseconds>
				</FlowInstance>
				<FlowInstance>
					<flowInstanceID>145263</flowInstanceID>
					<added>2024-03-15 14:40</added>
					<updated>2024-03-18 09:52</updated>
					<firstSubmitted>2024-03-15 14:44</firstSubmitted>
				</FlowInstance>
			</FlowInstances>
			""", "UTF-8");

		final var result = mapper.mapFlowIds(inputStream);

		assertThat(result).isNotNull().hasSize(3).containsExactlyInAnyOrder("172698", "172529", "145263");
	}

	@Test
	void mapFlowIdsWithNoFlowInstances() {
		final var inputStream = IOUtils.toInputStream("""
			<FlowInstances>
			</FlowInstances>
			""", "UTF-8");

		final var result = mapper.mapFlowIds(inputStream);

		// Assert
		assertThat(result).isNotNull().isEmpty();
	}

	@Test
	void mapFlowInstance_Elevresa() throws IOException {
		// Arrange
		final Path path = Paths.get("src/test/resources/mockfiles/errand_elevresa.xml");
		final var bytes = Files.readAllBytes(path);

		// Act
		final var result = mapper.mapFlowInstance(bytes, FamilyType.ELEVRESA);

		// Assert
		assertThat(result).isNotNull().hasNoNullFieldsOrPropertiesExcept("administratorName",
			"contactInfo",
			"emailStatus",
			"status",
			"smsStatus",
			"daycarePlacement",
			"daycarePlacementChanged",
			"daycarePlacementChangedFrom");
	}

	@ParameterizedTest
	@ValueSource(strings = {
		"errand_skolskjuts_valdbarn.xml", "errand_skolskjuts.xml", "errand_skolskjuts_BarnetsUppgifter.xml"
	})
	void mapFlowInstance_Skolskjuts(final String filename) throws IOException {
		// Arrange
		final Path path = Paths.get("src/test/resources/mockfiles/" + filename);
		final var bytes = Files.readAllBytes(path);

		// Act
		final var result = mapper.mapFlowInstance(bytes, FamilyType.SKOLSKJUTS);

		// Assert
		assertThat(result).isNotNull().hasNoNullFieldsOrPropertiesExcept("schoolUnit", "emailStatus", "smsStatus", "status");
	}

	@Test
	void mapFlowInstance_skolskjuts_empty_uppgifterOmBarnet() throws IOException {
		// Arrange
		final Path path = Paths.get("src/test/resources/mockfiles/errand_skolskjuts_empty_uppgifterOmBarnet.xml");
		final var bytes = Files.readAllBytes(path);

		// Act
		final var result = mapper.mapFlowInstance(bytes, FamilyType.SKOLSKJUTS);

		// Assert
		assertThat(result).isNotNull().hasNoNullFieldsOrPropertiesExcept("schoolUnit", "status", "emailStatus", "smsStatus", "minorIdentifier", "minorName");
	}

	@ParameterizedTest
	@EnumSource(FamilyType.class)
	void mapFlowInstanceWithNullValues(final FamilyType familyType) {
		// Arrange
		final var flowInstance = """
			<FlowInstance>
			</FlowInstance>
			""";

		// Act
		final var result = mapper.mapFlowInstance(flowInstance.getBytes(), familyType);

		// Assert
		assertThat(result).isNotNull().hasAllNullFieldsOrProperties();
	}

	@Test
	void mapStatus() {
		// Arrange
		final var status = """
			<Status>
			<statusID>123</statusID>
			<name>Beslut som upphört att gälla</name>
			<newExternalMessagesDisallowed>false</newExternalMessagesDisallowed>
			<addExternalMessage>false</addExternalMessage>
			<addInternalMessage>false</addInternalMessage>
			<isRestrictedAdminDeletable>false</isRestrictedAdminDeletable>
			<contentType>ARCHIVED</contentType>
			</Status>
			""";
		// Act
		final var result = mapper.mapStatus(status.getBytes());

		// Assert
		assertThat(result).isEqualTo("Beslut som upphört att gälla");
	}

	@Test
	void mapStatusWithNullValues() {
		// Arrange
		final var status = "";
		// Act
		final var result = mapper.mapStatus(status.getBytes());

		// Assert
		assertThat(result).isNull();
	}

}
