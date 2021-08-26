package example;

import io.micronaut.context.Qualifier;
import io.micronaut.inject.BeanType;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.stream.Stream;

@MicronautTest
class ExampleTest {

    @Inject
    EmbeddedApplication<?> application;

    @Test
    void testAnnotation() {
        Optional<ExampleBean> exampleBean = application.getApplicationContext().findBean(ExampleBean.class, new Qualifier<ExampleBean>() {
            @Override
            public <BT extends BeanType<ExampleBean>> Stream<BT> reduce(Class<ExampleBean> beanType, Stream<BT> candidates) {
                return candidates.filter(this::filter);
            }

            <BT extends BeanType<ExampleBean>> boolean filter(BT bt) {
                return bt.isAnnotationPresent(RegularAnnotation.class);
            }

        });
        Assertions.assertTrue(exampleBean.isPresent());
    }

    @Test
    void testNestedAnnotation() {
        Optional<ExampleBean> exampleBean = application.getApplicationContext().findBean(ExampleBean.class, new Qualifier<ExampleBean>() {
            @Override
            public <BT extends BeanType<ExampleBean>> Stream<BT> reduce(Class<ExampleBean> beanType, Stream<BT> candidates) {
                return candidates.filter(this::filter);
            }

            <BT extends BeanType<ExampleBean>> boolean filter(BT bt) {
                return bt.isAnnotationPresent(Application.ExampleAnnotation.class);
            }

        });
        Assertions.assertTrue(exampleBean.isPresent());
    }
}
