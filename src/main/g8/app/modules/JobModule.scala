package modules

import jobs.Job
import jobs.circuiBreaker.JobStarter
import play.api.inject.{SimpleModule, bind, _}

class JobModule extends SimpleModule(
  bind[Job].toSelf.eagerly(),
  bind[JobStarter].toSelf.eagerly()
)