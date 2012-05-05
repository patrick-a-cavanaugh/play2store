package controllers.admin;

import actions.TemplateVars;
import be.objectify.deadbolt.actions.Restrict;
import models.Image;
import models.Product;
import play.mvc.*;
import play.data.*;
import play.*;
import views.html.admin.products.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;

@Restrict("admin")
@With(TemplateVars.class)
public class Products extends Controller {
    public static Result index() {
        return ok(index.render(Product.find.join("category", "*").findList()));
    }

    public static Result create() {
        Form<Product> productForm = form(Product.class);
        return ok(
            createForm.render(productForm)
        );
    }

    public static Result save() {
        Form<Product> productForm = form(Product.class).bindFromRequest(
                // TODO: list allowed fields here.
        );

        if(!productForm.hasErrors()) {
            if(productForm.get().category != null
                    && productForm.get().category.id == null) {
                productForm.reject("category.id", "You must select a category for this product.");
            }
        }

        String fileName = null;
        String contentType = null;
        File file = null;
        if (!productForm.hasErrors()) {
            Http.MultipartFormData body = request().body().asMultipartFormData();
            Http.MultipartFormData.FilePart picture = body.getFile("image");
            if (picture != null) {
                fileName = picture.getFilename();
                contentType = picture.getContentType();
                file = picture.getFile();

                if (! Arrays.asList("image/jpeg", "image/png").contains(contentType)) {
                    productForm.reject("image", "The image must be a JPEG or PNG image.");
                }
            }
        }

        if (productForm.hasErrors()) {
            return badRequest(createForm.render(productForm));
        } else {
            Product product = productForm.get();
            if (file != null) {
                // TODO move this out above the if ( hasErrors ) to allow the reject to work
                product.image = new Image();
                product.image.name = fileName;
                product.image.mimeType = contentType;
                try {
                    FileInputStream inputStream = new FileInputStream(file);
                    product.image.data = new byte[(int)file.length()];
                    //noinspection ResultOfMethodCallIgnored
                    inputStream.read(product.image.data);

                    // Now find the height and width of the image
                    BufferedImage image = ImageIO.read(new ByteArrayInputStream(product.image.data));
                    if (image == null) {
                        productForm.reject("image", "Unable to read the supplied image");
                    } else {
                        product.image.width = image.getWidth();
                        product.image.height = image.getHeight();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Product.create(product);
            return redirect(routes.Products.index());
        }
    }
}
