package controllers;

import models.Image;
import play.*;
import play.mvc.*;

import java.io.ByteArrayInputStream;

public class Images extends Controller {
    public static Result show(Long id, String name) {
        Image image = Image.find.ref(id);

        if (image != null) {
            ByteArrayInputStream stream = new ByteArrayInputStream(image.data);
            return ok(stream).as(image.mimeType);
        } else {
            return notFound("We could not find the image you requested.");
        }
    }
}
