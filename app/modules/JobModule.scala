package modules

import jobs.Job
import play.api.inject.{ SimpleModule, _ }

class JobModule extends SimpleModule(bind[Job].toSelf.eagerly())