package edu.eci.dockerLab.framework;

public interface WebMethod {
    String execute(HttpRequest req, HttpResponse res);
}
