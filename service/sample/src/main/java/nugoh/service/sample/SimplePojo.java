package nugoh.service.sample;


import java.util.Map;

public class SimplePojo {

    private String name;
    private String helloInit;

    public void setName(String name) {
        this.name = name;
    }

    public String getHelloInit() {
        return helloInit;
    }

    public void init() {
        helloInit = "hello " + name;
    }

    public void run(Map<String, Object> context) {
        System.out.println("----------> " + helloInit + " <----------");
        context.put("helloInit", helloInit);
    }
}