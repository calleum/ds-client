public class JobUtils {

    public static Job createJobFromJobN(final String jobnResponse) {
        return JobUtils.parseJobTokens(jobnResponse.trim().split("\\s+"));
    }

    private static Job parseJobTokens(final String[] serverJobTokens) {
        final Job j = new Job();
        j.setId(Integer.parseInt(serverJobTokens[2]));
        j.setEstRuntime(Integer.parseInt(serverJobTokens[3]));
        j.setSubmissionTime(Integer.parseInt(serverJobTokens[1]));
        j.setNumCores(Integer.parseInt(serverJobTokens[4]));
        j.setMemory(Integer.parseInt(serverJobTokens[5]));
        j.setDiskSpace(Integer.parseInt(serverJobTokens[6]));

        return j;
    }
   }
