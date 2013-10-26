package server;

import java.util.ArrayList;

import communicator.DownloadBatchParams;
import communicator.DownloadBatchResult;
import communicator.DownloadFileParams;
import communicator.DownloadFileResult;
import communicator.GetFieldsParams;
import communicator.GetFieldsResult;
import communicator.GetProjectsResult;
import communicator.GetSampleImageParams;
import communicator.GetSampleImageResult;
import communicator.SearchParams;
import communicator.SearchResult;
import communicator.SubmitBatchParams;
import communicator.SubmitBatchResult;
import communicator.ValidateUserParams;
import communicator.ValidateUserResult;

public class ServerFromClient implements ClientCommunitcatorInterface {

	@Override
	public ValidateUserResult validateUser(ValidateUserParams params)throws InvalidCredentialsException, FailedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<GetProjectsResult> getProjects(ValidateUserParams params)throws FailedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GetSampleImageResult getSampleImage(GetSampleImageParams params)throws FailedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DownloadBatchResult downloadBatch(DownloadBatchParams params)throws FailedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SubmitBatchResult submitBatch(SubmitBatchParams params)throws FailedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<GetFieldsResult> getFields(GetFieldsParams params)throws FailedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<SearchResult> search(SearchParams params)throws FailedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DownloadFileResult downloadFile(DownloadFileParams params)throws FailedException {
		// TODO Auto-generated method stub
		return null;
	}

}
