public class JobUtils {

    public static Job createJobFromJobN(final String jobnResponse) {
        return JobUtils.parseJobTokens(jobnResponse.trim().split("\\s+"));
    }

    public static Job parseJobTokens(String[] serverJobTokens) {
        Job j = new Job();
        j.setId(Integer.parseInt(serverJobTokens[2]));
        j.setEstRuntime(Integer.parseInt(serverJobTokens[1]));
        j.setNumCores(Integer.parseInt(serverJobTokens[3]));
        j.setMemory(Integer.parseInt(serverJobTokens[4]));
        j.setDiskSpace(Integer.parseInt(serverJobTokens[5]));

        return j;
    }

}
