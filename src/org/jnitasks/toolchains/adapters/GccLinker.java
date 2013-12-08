package org.jnitasks.toolchains.adapters;

import org.jnitasks.toolchains.LinkerAdapter;
import org.jnitasks.types.AbstractFeature;
import org.jnitasks.types.Define;
import org.jnitasks.types.Include;
import org.jnitasks.types.Library;

import java.util.Iterator;

public class GccLinker extends LinkerAdapter {
	public GccLinker() {
		super();
		super.executable = "gcc";
	}

	public String describeCommand() {
		StringBuilder command = new StringBuilder(this.getExecutable());

		for (AbstractFeature feat : features) {
			if (feat instanceof Define) {
				Define def = (Define) feat;

				command.append(" -D").append(def.getName());
				if (def.getValue() != null) {
					command.append('=').append(def.getValue());
				}
			}
			else if (feat instanceof Include) {
				Include inc = (Include) feat;

				command.append(" -I").append(inc.getPath());
			}
			else if (feat instanceof Library) {
				Library lib = (Library) feat;

				if (lib.getPath() != null) {
					command.append(" -L").append(lib.getPath().getPath());
				}

				if (lib.getLib() != null) {
					command.append(" -l").append(lib.getLib());
				}
			}
			else if (feat instanceof LinkerAdapter.Argument) {
				Argument arg = (Argument) feat;

				command.append(" -Wl,").append(arg.getValue());
			}

			Iterator<String> files = this.getInFiles();
			while (files.hasNext()) {
				command.append(' ').append(files.next());
			}

			command.append(" -o").append(this.getOutFile());

			return command.toString();
		}

		return command.toString();
	}
}