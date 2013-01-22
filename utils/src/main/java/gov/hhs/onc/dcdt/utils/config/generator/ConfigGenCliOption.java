package gov.hhs.onc.dcdt.utils.config.generator;

import gov.hhs.onc.dcdt.utils.cli.CliOption;
import gov.hhs.onc.dcdt.utils.cli.validation.NotBlankValidator;
import gov.hhs.onc.dcdt.utils.cli.validation.PositiveIntegerValidator;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.builder.ArgumentBuilder;
import org.apache.commons.cli2.builder.DefaultOptionBuilder;
import org.apache.commons.cli2.builder.SwitchBuilder;
import org.apache.commons.cli2.option.Switch;

public enum ConfigGenCliOption implements CliOption
{
	DOMAIN
	(
		new DefaultOptionBuilder()
			.withDescription("Domain name of the deployment.")
			.withShortName("d")
			.withLongName("domain")
			.withArgument(new ArgumentBuilder()
				.withName("name")
				.withMinimum(1)
				.withMaximum(1)
				.withValidator(NotBlankValidator.INSTANCE)
				.create())
			.withRequired(true)
			.create(), 
		"domain"
	), 
	OUTPUT_FILE
	(
		new DefaultOptionBuilder()
			.withDescription("Path to the output archive file.")
			.withShortName("o")
			.withLongName("out")
			.withArgument(new ArgumentBuilder()
				.withName("path")
				.withMinimum(1)
				.withMaximum(1)
				.withValidator(NotBlankValidator.INSTANCE)
				.create())
			.create(), 
		"outputFile"
	), 
	RESULT_MAIL_ADDRESS
	(
		new DefaultOptionBuilder()
			.withDescription("Result mail address.")
			.withLongName("ra")
			.withLongName("resaddr")
			.withArgument(new ArgumentBuilder()
				.withName("address")
				.withMinimum(1)
				.withMaximum(1)
				.withValidator(NotBlankValidator.INSTANCE)
				.create())
			.withRequired(true)
			.create(), 
		"resultMailAddress"
	), 
	RESULT_MAIL_PASSWORD
	(
		new DefaultOptionBuilder()
			.withDescription("Result mail password.")
			.withLongName("rps")
			.withLongName("respass")
			.withArgument(new ArgumentBuilder()
				.withName("pass")
				.withMinimum(1)
				.withMaximum(1)
				.withValidator(NotBlankValidator.INSTANCE)
				.create())
			.withRequired(true)
			.create(), 
		"resultMailPassword"
	), 
	RESULT_MAIL_HOST
	(
		new DefaultOptionBuilder()
			.withDescription("Result mail host.")
			.withLongName("rh")
			.withLongName("reshost")
			.withArgument(new ArgumentBuilder()
				.withName("host")
				.withMinimum(1)
				.withMaximum(1)
				.withValidator(NotBlankValidator.INSTANCE)
				.create())
			.withRequired(true)
			.create(), 
		"resultMailHost"
	), 
	RESULT_MAIL_PORT
	(
		new DefaultOptionBuilder()
			.withDescription("Result mail port.")
			.withLongName("rp")
			.withLongName("resport")
			.withArgument(new ArgumentBuilder()
				.withName("num")
				.withMinimum(1)
				.withMaximum(1)
				.withValidator(PositiveIntegerValidator.INSTANCE)
				.create())
			.create(), 
		"resultMailPort"
	), 
	RESULT_MAIL_USE_SSL
	(
		new SwitchBuilder(Switch.DEFAULT_DISABLED_PREFIX, Switch.DEFAULT_ENABLED_PREFIX)
			.withDescription("Result mail SSL usage.")
			.withName("rs")
			.withName("resssl")
			.withSwitchDefault(true)
			.create(), 
		"resultMailUseSsl"
	);
	
	private Option option;
	private String attribName;
	
	ConfigGenCliOption(Option option)
	{
		this(option, null);
	}
	
	ConfigGenCliOption(Option option, String attribName)
	{
		this.option = option;
		this.attribName = attribName;
	}
	
	@Override
	public Option getOption()
	{
		return this.option;
	}
	
	@Override
	public String getAttribName()
	{
		return this.attribName;
	}
}