import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

import java.io.File;
import java.io.FileInputStream;

import javax.xml.transform.stream.StreamSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import binji.demo.data.xml.ovd.Ovd;
import binji.demo.services.ServicesConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ServicesConfig.class})
public class JaxMarshallerIT {
	
	@Autowired private Jaxb2Marshaller jax;

	@Test
	public void getFirstItem() throws Exception {
		 File file = new File("E:\\work\\uca\\dev\\ces_demo\\gracenote\\ovd\\on_samp_ovd_v4.0_20171221.xml");
		Ovd ovd = (Ovd)jax.unmarshal(new StreamSource(new FileInputStream(file)));
		assertThat(ovd, notNullValue());
		assertThat(ovd.getOnlineMovies().getMovie().stream()
				.filter(m -> m.getTitle() == "21 Jump Street").findAny(), notNullValue());
	}
	
}
