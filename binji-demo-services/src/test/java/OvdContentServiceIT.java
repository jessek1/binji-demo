import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import binji.demo.common.dto.ovd.OvdContentBase;
import binji.demo.core.configuration.RootConfig;
import binji.demo.services.ovd.OvdContentService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RootConfig.class})
public class OvdContentServiceIT {

	@Autowired
	private OvdContentService ovdContentService;
	
	@Test
	public void testImages() throws Exception {
		//series
		//SH000002920000
		
		OvdContentBase content = ovdContentService.getOvdContentByTmsId("MV003744420000");
		if (content == null)
			throw new Exception();
	}
}



