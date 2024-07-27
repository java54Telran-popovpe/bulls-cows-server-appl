package telran.bullsCows;

import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONObject;

import telran.net.Protocol;
import telran.net.Request;
import telran.net.Response;
import telran.net.ResponseCode;

public class BullsCowsProtocol implements Protocol {

	BullsCowsService bullsCowsService;

	public BullsCowsProtocol(BullsCowsService bullsCowsService) {
		this.bullsCowsService = bullsCowsService;
	}

	@Override
	public Response getResponse(Request request) {
		String requestType = request.requestType();
		String requestData = request.requestData();
		Response response = null;
		try {
			response = switch (requestType) {
			case "createNewGame" -> createNewGame(requestData);
			case "getResults" -> getResults(requestData);
			case "isGameOver" -> isGameOver(requestData);
			default -> wrongTypeResponse(requestType);
			};
		} catch (Exception e) {
			response = wrongDataResponse(e.getMessage());
		}
		return response;
	}

	private Response wrongDataResponse(String message) {
		return new Response(ResponseCode.WRONG_REQUEST_DATA, message);
	}

	private Response wrongTypeResponse(String message) {
		return new Response(ResponseCode.WRONG_REQUAST_TYPE, message);
	}

	private Response createNewGame(String message) {
		long gameId = bullsCowsService.createNewGame();
		return new Response(ResponseCode.OK, Long.toUnsignedString(gameId));
	}

	private Response getResults( String requestData ) {
		JSONObject jsonObject = new JSONObject( requestData );
		long gameID = Long.parseUnsignedLong(jsonObject.getString("gameID"));
		JSONObject moveJSON = jsonObject.getJSONObject("move");
		String clientSeq = moveJSON.getString("clientSeq");
		long moveGameID = Long.parseUnsignedLong(moveJSON.getString("gameID"));
		List<MoveResult> result = bullsCowsService.getResults(gameID, new Move( moveGameID, clientSeq));
		return new Response(ResponseCode.OK, result.stream().map(MoveResult::getJSON).collect(Collectors.joining(";")));
	}
	
	private Response isGameOver(String gameID ) {
		long id = Long.parseUnsignedLong(gameID);
		return new Response(ResponseCode.OK, Boolean.toString(bullsCowsService.isGameOver(id)));
	}

}
