import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import binji.demo.common.model.gracenote.ProgramImage;
import binji.demo.core.configuration.RootConfig;
import binji.demo.services.ovd.GracenoteService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RootConfig.class})
public class GracenoteServiceIT {

	@Autowired
	private GracenoteService gracenoteService;
	
	@Test
	public void testImages() throws Exception {
		List<ProgramImage> images = gracenoteService.getProgramImages("MV003744420000");
		if (images == null)
			throw new Exception();
	}
}



