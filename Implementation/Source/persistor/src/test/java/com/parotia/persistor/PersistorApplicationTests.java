package com.parotia.persistor;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.parotia.persistor.model.TradeEvent;
import com.parotia.persistor.model.TradeRequest;
import com.parotia.persistor.model.TradeSymbol;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PersistorApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	private String baseUrl;

	@BeforeEach
	void setup() {
		baseUrl = "http://localhost:" + port + "/api/v1/trades";
	}

	@Test
	void contextLoads() {
		log.info("Context loads successfully");
	}

	@Test
	void healthEndpointUp() {
		ResponseEntity<String> response = restTemplate.getForEntity(
				"http://localhost:" + port + "/actuator/health", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).contains("\"status\":\"UP\"");
	}

	@Test
	void createAndRetrieveTrade() {
		// Create
		TradeRequest request = sampleRequest("BTCUSDT");
		TradeEvent created = restTemplate.postForObject(baseUrl, request, TradeEvent.class);
		assertThat(created.getId()).isNotNull();
		assertThat(created.getSymbol()).isEqualTo(TradeSymbol.BTCUSDT);

		// Retrieve by ID
		ResponseEntity<TradeEvent> getResponse = restTemplate.getForEntity(
				baseUrl + "/" + created.getId(), TradeEvent.class);
		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		TradeEvent fetched = getResponse.getBody();
		assertThat(fetched.getId()).isEqualTo(created.getId());
	}

	@Test
	void listTradesPaginationAndFilter() {
		// Create two trades
		TradeEvent t1 = restTemplate.postForObject(baseUrl, sampleRequest("BTCUSDT"), TradeEvent.class);
		TradeEvent t2 = restTemplate.postForObject(baseUrl, sampleRequest("ETHUSDT"), TradeEvent.class);

		// List all
		ResponseEntity<Map<String, Object>> listAll = restTemplate.exchange(
				baseUrl + "?page=0&size=10",
				HttpMethod.GET, null,
				new ParameterizedTypeReference<>() {
				});
		assertThat(listAll.getStatusCode()).isEqualTo(HttpStatus.OK);
		List<?> contentAll = (List<?>) listAll.getBody().get("content");
		assertThat(contentAll.size()).isGreaterThanOrEqualTo(2);

		// Filter by symbol
		ResponseEntity<Map<String, Object>> filtered = restTemplate.exchange(
				baseUrl + "?symbol=ETHUSDT&page=0&size=5",
				HttpMethod.GET, null,
				new ParameterizedTypeReference<>() {
				});
		assertThat(filtered.getStatusCode()).isEqualTo(HttpStatus.OK);
		List<?> contentFiltered = (List<?>) filtered.getBody().get("content");
		assertThat(contentFiltered).allMatch(item -> ((Map<?, ?>) item).get("symbol").equals("ETHUSDT"));
	}

	@Test
	void updateTradeSuccessAndNotFound() {
		// Create
		TradeEvent created = restTemplate.postForObject(baseUrl, sampleRequest("ETHUSDT"), TradeEvent.class);

		// Update existing
		TradeRequest updateReq = sampleRequest("ETHUSDT");
		HttpEntity<TradeRequest> entity = new HttpEntity<>(updateReq);
		ResponseEntity<TradeEvent> updateResponse = restTemplate.exchange(
				baseUrl + "/" + created.getId(), HttpMethod.PUT, entity, TradeEvent.class);
		assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(updateResponse.getBody().getSymbol()).isEqualTo(TradeSymbol.ETHUSDT);

		// Update non-existing
		ResponseEntity<Void> notFound = restTemplate.exchange(
				baseUrl + "/99999", HttpMethod.PUT, entity, Void.class);
		assertThat(notFound.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	void deleteTradeSuccessAndNotFound() {
		// Create
		TradeEvent created = restTemplate.postForObject(baseUrl, sampleRequest("BTCUSDT"), TradeEvent.class);

		// Delete existing
		ResponseEntity<Void> deleteResp = restTemplate.exchange(
				baseUrl + "/" + created.getId(), HttpMethod.DELETE, null, Void.class);
		assertThat(deleteResp.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

		// Delete again (not found)
		ResponseEntity<Void> deleteNotFound = restTemplate.exchange(
				baseUrl + "/" + created.getId(), HttpMethod.DELETE, null, Void.class);
		assertThat(deleteNotFound.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	private TradeRequest sampleRequest(String symbol) {
		return TradeRequest.builder()
				.eventTime(System.currentTimeMillis())
				.symbol(symbol)
				.price(new BigDecimal("100.00"))
				.quantity(new BigDecimal("1.0"))
				.tradeTime(System.currentTimeMillis())
				.isMarketMaker(true)
				.isIgnored(false)
				.build();
	}
}
