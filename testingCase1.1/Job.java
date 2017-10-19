public class Job {
    Worker employee;
    int a;
    public job ( ){
        employee = new Worker();
    }

    public void setDefaultiy(String hard) {
        if(hard.equals("hard")){
            employee.setEmotion("sad");
        }
    }
}
