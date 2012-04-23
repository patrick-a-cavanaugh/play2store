// from https://gist.github.com/2368789
package controllers;

import models.Model;
import models.User;
import play.api.templates.Html;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public abstract class ControllerAgent<T extends Model, F1, F2> {

    protected Form<F1> composeForm;
    protected Form<F2> editForm;

    public ControllerAgent(Class<F1> clazzComposeForm, Class<F2> clazzEditForm) {
        this.composeForm = Controller.form(clazzComposeForm);
        this.editForm = Controller.form(clazzEditForm);
    }

    protected abstract Html displayCompose(Form<F1> composeForm, Long... preIds);

    protected abstract Html displayItem(T element, Long... preIds);

    protected abstract Html displayEditForm(Form<F2> editForm, T element, Long... preIds);

    protected abstract T createFromForm(Form<F1> composeForm, Long... preIds);

    protected abstract T editFromForm(T element, Form<F2> editForm);

    protected abstract F2 translateItemToEditFormInstance(T element);

    public Result compose(Long... preIds) {
        return Controller.ok(displayCompose(composeForm, preIds));
    }

    public Result create(Long... preIds) {
        Form<F1> filledForm = composeForm.bindFromRequest();
        if (filledForm.hasErrors()) return Controller.status(409, displayCompose(filledForm, preIds));
        T element = createFromForm(filledForm, preIds);
        return Controller.ok(displayItem(element, preIds));
    }

    public Result edit(T element, Long... preIds) {
        if (element == null) return Controller.notFound();
        return Controller.ok(displayEditForm(editForm.fill(translateItemToEditFormInstance(element)), element, preIds));
    }

    public Result update(T element, Long... preIds) {
        if (element == null) return Controller.notFound();
        Form<F2> filledForm = editForm.bindFromRequest();
        if (filledForm.hasErrors()) return Controller.status(409, displayEditForm(filledForm, element, preIds));
        T editItem = editFromForm(element, filledForm);
        return Controller.ok(displayItem(editItem, preIds));
    }

    public Result delete(T element, User user) {
        if (element == null) return Controller.notFound();
        if (element.deleteBy(user)) return Controller.noContent();
        else return Controller.unauthorized();
    }

    public abstract static class DefaultControllerAgent<T extends Model> extends ControllerAgent<T, T, T> {


        public DefaultControllerAgent(Class<T> clazz) {
            super(clazz, clazz);
        }

        protected abstract T createItemFromForm(T element, Long... preIds);

        @Override
        protected T createFromForm(Form<T> composeForm, Long... preIds) {
            return createItemFromForm(composeForm.get(), preIds);
        }

        @Override
        protected T editFromForm(T element, Form<T> editForm) {
            T editItem = editForm.get();
            editItem.update(element.id);
            return editItem;
        }

        @Override
        protected T translateItemToEditFormInstance(T element) {
            return element;
        }

        public Result delete(T element) {
            throw new RuntimeException("Not implemented");
            //return delete(element, Auth.getUser());
        }

    }

}
