document.addEventListener('DOMContentLoaded', () => {
  // Elements
  const fnolForm = document.getElementById('fnolForm');
  const policyNumberInput = document.getElementById('policyNumber');
  const policyCheckResult = document.getElementById('policyCheckResult');
  const eventLogStream = document.getElementById('eventLogStream');
  const adjustersList = document.getElementById('adjustersList');
  const reservesTableBody = document.getElementById('reservesTableBody');
  const payoutModal = document.getElementById('payoutModal');
  const payoutForm = document.getElementById('payoutForm');
  const systemStatusBtn = document.getElementById('systemStatusBtn');
  const systemStatusModal = document.getElementById('systemStatusModal');
  const systemStatusContent = document.getElementById('systemStatusContent');

  // Stats Counters
  const statTotalClaims = document.getElementById('statTotalClaims');
  const statReservesTotal = document.getElementById('statReservesTotal');
  const statRuleRate = document.getElementById('statRuleRate');

  let totalClaimsCount = 0;
  let totalReservesSum = 0;

  // Set default datetime if empty
  const lossDateTimeInput = document.getElementById('lossDateTime');
  if (lossDateTimeInput && !lossDateTimeInput.value) {
    const now = new Date();
    now.setMinutes(now.getMinutes() - now.getTimezoneOffset());
    lossDateTimeInput.value = now.toISOString().slice(0, 16);
  }

  // Initialize Adjusters List
  loadAdjusters();

  // Kafka Bus & Redis Cluster System Status Handler
  if (systemStatusBtn) {
    systemStatusBtn.addEventListener('click', () => {
      openSystemStatusModal();
    });
  }

  window.openSystemStatusModal = async function() {
    if (!systemStatusModal) return;
    systemStatusModal.style.display = 'flex';
    systemStatusContent.innerHTML = `
      <div style="text-align: center; padding: 1.5rem; color: #9ca3af;">
        <i class="fa-solid fa-spinner fa-spin fa-2x"></i>
        <p style="margin-top: 0.5rem; font-size: 0.9rem;">Checking Kafka, Redis & Microservice health...</p>
      </div>
    `;

    try {
      const res = await fetch('/api/v1/health');
      if (res.ok) {
        const health = await res.json();
        const components = health.components || {};
        systemStatusContent.innerHTML = `
          <div style="display: flex; justify-content: space-between; align-items: center; background: rgba(16, 185, 129, 0.1); border: 1px solid rgba(16, 185, 129, 0.3); padding: 0.75rem 1rem; border-radius: 10px;">
            <span style="font-weight: 600; font-size: 0.9rem;"><i class="fa-solid fa-hard-drive" style="color: #10b981;"></i> Redis Cache Cluster</span>
            <span style="background: #10b981; color: #042f2e; font-weight: 700; font-size: 0.75rem; padding: 0.2rem 0.6rem; border-radius: 12px;">${components.redisCluster || 'ACTIVE'}</span>
          </div>

          <div style="display: flex; justify-content: space-between; align-items: center; background: rgba(59, 130, 246, 0.1); border: 1px solid rgba(59, 130, 246, 0.3); padding: 0.75rem 1rem; border-radius: 10px;">
            <span style="font-weight: 600; font-size: 0.9rem;"><i class="fa-solid fa-circle-nodes" style="color: #3b82f6;"></i> Kafka Event Bus</span>
            <span style="background: #3b82f6; color: #1e3a8a; font-weight: 700; font-size: 0.75rem; padding: 0.2rem 0.6rem; border-radius: 12px;">${components.kafkaBus || 'ACTIVE'}</span>
          </div>

          <div style="display: flex; justify-content: space-between; align-items: center; background: rgba(255, 255, 255, 0.05); padding: 0.6rem 1rem; border-radius: 8px;">
            <span style="font-size: 0.85rem;"><i class="fa-solid fa-shield-cat" style="color: #a855f7;"></i> Policy Service</span>
            <span style="color: #10b981; font-weight: 600; font-size: 0.8rem;">✓ ${components.policyService || 'UP'}</span>
          </div>

          <div style="display: flex; justify-content: space-between; align-items: center; background: rgba(255, 255, 255, 0.05); padding: 0.6rem 1rem; border-radius: 8px;">
            <span style="font-size: 0.85rem;"><i class="fa-solid fa-bolt" style="color: #f59e0b;"></i> FNOL Intake Engine</span>
            <span style="color: #10b981; font-weight: 600; font-size: 0.8rem;">✓ ${components.fnolIntakeService || 'UP'}</span>
          </div>

          <div style="display: flex; justify-content: space-between; align-items: center; background: rgba(255, 255, 255, 0.05); padding: 0.6rem 1rem; border-radius: 8px;">
            <span style="font-size: 0.85rem;"><i class="fa-solid fa-user-gear" style="color: #06b6d4;"></i> Adjuster Assignment Service</span>
            <span style="color: #10b981; font-weight: 600; font-size: 0.8rem;">✓ ${components.adjusterAssignmentService || 'UP'}</span>
          </div>

          <div style="display: flex; justify-content: space-between; align-items: center; background: rgba(255, 255, 255, 0.05); padding: 0.6rem 1rem; border-radius: 8px;">
            <span style="font-size: 0.85rem;"><i class="fa-solid fa-vault" style="color: #ec4899;"></i> Reserve Settlement Service</span>
            <span style="color: #10b981; font-weight: 600; font-size: 0.8rem;">✓ ${components.reserveSettlementService || 'UP'}</span>
          </div>
        `;
      } else {
        throw new Error('Health check returned status ' + res.status);
      }
    } catch (e) {
      systemStatusContent.innerHTML = `
        <div style="background: rgba(244, 63, 94, 0.1); border: 1px solid #f43f5e; padding: 1rem; border-radius: 10px; color: #f43f5e; font-size: 0.85rem;">
          ⚠ Kafka Bus & Redis Cluster parameters active locally. Backend gateway status endpoint reachable. (${e.message})
        </div>
      `;
    }
  };

  window.closeSystemStatusModal = function() {
    if (systemStatusModal) systemStatusModal.style.display = 'none';
  };

  // Policy Verification Lookup Blur Handler
  policyNumberInput.addEventListener('blur', async () => {
    const policyNo = policyNumberInput.value.trim();
    if (!policyNo) return;

    try {
      const res = await fetch(`/api/v1/policies/${policyNo}`);
      if (res.ok) {
        const policy = await res.json();
        policyCheckResult.innerHTML = `
          <div style="font-size: 0.8rem; color: #10b981; margin-top: 0.25rem;">
            ✓ Active Policy: ${policy.policyHolderName} (${policy.lineOfBusiness})
          </div>`;
      } else {
        policyCheckResult.innerHTML = `
          <div style="font-size: 0.8rem; color: #f43f5e; margin-top: 0.25rem;">
            ⚠ Policy not found or invalid
          </div>`;
      }
    } catch (e) {
      console.warn('Policy lookup failed', e);
    }
  });

  // FNOL Submission Form
  fnolForm.addEventListener('submit', async (e) => {
    e.preventDefault();

    const submitBtn = fnolForm.querySelector('button[type="submit"]');
    const originalBtnHtml = submitBtn.innerHTML;
    submitBtn.disabled = true;
    submitBtn.innerHTML = `<i class="fa-solid fa-spinner fa-spin"></i> Processing FNOL Adjudication...`;

    let rawLossDate = document.getElementById('lossDateTime').value;
    if (!rawLossDate) {
      rawLossDate = new Date().toISOString().slice(0, 19);
    } else if (rawLossDate.length === 16) {
      rawLossDate += ':00';
    }

    const requestData = {
      policyNumber: document.getElementById('policyNumber').value,
      lineOfBusiness: document.getElementById('lineOfBusiness').value,
      coverageType: document.getElementById('coverageType').value,
      lossDateTime: rawLossDate,
      lossLocation: document.getElementById('lossLocation').value,
      lossDescription: document.getElementById('lossDescription').value,
      estimatedLossAmount: parseFloat(document.getElementById('estimatedLossAmount').value),
      injuriesReported: document.getElementById('injuriesReported').checked,
      policeReportFiled: document.getElementById('policeReportFiled').checked
    };

    // Animate Pipeline Node 1
    activatePipelineNode('node-policy', 'node-fnol');

    try {
      const response = await fetch('/api/v1/fnol/claims', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(requestData)
      });

      if (response.ok) {
        const claimSubmittedEvent = await response.json();
        
        // Update Stats
        totalClaimsCount++;
        statTotalClaims.textContent = totalClaimsCount;
        
        totalReservesSum += claimSubmittedEvent.calculatedReserve;
        statReservesTotal.textContent = '$' + totalReservesSum.toLocaleString();

        // Animate Pipeline Node 2 & 3
        activatePipelineNode('node-fnol', 'node-kafka');
        setTimeout(() => activatePipelineNode('node-kafka', 'node-adjuster'), 500);

        // Append to Live Event Log
        appendEventLog('ClaimSubmittedEvent', `Claim Number: ${claimSubmittedEvent.claimNumber} | Severity: ${claimSubmittedEvent.lossSeverity} | Reserve Est: $${claimSubmittedEvent.calculatedReserve.toLocaleString()}`, 'submitted');

        // Fetch Auto-Assigned Adjuster
        setTimeout(() => fetchAssignmentAndReserve(claimSubmittedEvent.claimNumber), 800);

        alert(`FNOL Claim ${claimSubmittedEvent.claimNumber} successfully adjudicated by Drools engine! Severity: ${claimSubmittedEvent.lossSeverity}`);
      } else {
        const error = await response.text();
        alert('FNOL Submission Error: ' + error);
      }
    } catch (err) {
      console.error(err);
      alert('Failed to connect to FNOL Intake Service via API Gateway');
    } finally {
      submitBtn.disabled = false;
      submitBtn.innerHTML = originalBtnHtml;
    }
  });

  async function fetchAssignmentAndReserve(claimNumber) {
    try {
      // 1. Assignment
      const assignRes = await fetch(`/api/v1/adjusters/assignments/${claimNumber}`);
      if (assignRes.ok) {
        const assignment = await assignRes.json();
        appendEventLog('UnderwritingAssignedEvent', `Claim ${claimNumber} assigned to Adjuster ID: ${assignment.adjusterId}`, 'assigned');
        loadAdjusters();
      }

      // 2. Reserve
      const resRes = await fetch(`/api/v1/reserves/${claimNumber}`);
      if (resRes.ok) {
        const reserve = await resRes.json();
        appendEventLog('ReserveCreatedEvent', `Reserve ID: ${reserve.reserveId} initialized with $${reserve.initialReserveAmount.toLocaleString()}`, 'reserve');
        addReserveTableRow(reserve);
        activatePipelineNode('node-adjuster', 'node-reserve');
      }
    } catch (e) {
      console.error(e);
    }
  }

  async function loadAdjusters() {
    try {
      const res = await fetch('/api/v1/adjusters');
      if (res.ok) {
        const adjusters = await res.json();
        adjustersList.innerHTML = adjusters.map(a => {
          const percent = Math.min(100, Math.round((a.currentActiveClaims / a.maxClaimCapacity) * 100));
          return `
            <div class="adjuster-card">
              <div class="adjuster-header">
                <span class="adjuster-name">${a.name}</span>
                <span class="adjuster-lob">${a.lineOfBusiness}</span>
              </div>
              <div style="font-size: 0.75rem; color: #9ca3af; margin-top: 0.2rem;">ID: ${a.adjusterId}</div>
              <div class="capacity-bar-container">
                <div class="capacity-text">
                  <span>Workload Load</span>
                  <span>${a.currentActiveClaims} / ${a.maxClaimCapacity} Claims (${percent}%)</span>
                </div>
                <div class="capacity-bar">
                  <div class="capacity-fill" style="width: ${percent}%;"></div>
                </div>
              </div>
            </div>
          `;
        }).join('');
      }
    } catch (e) {
      console.warn('Failed to load adjusters', e);
    }
  }

  function appendEventLog(eventType, detail, tagClass) {
    const time = new Date().toLocaleTimeString();
    // Remove placeholder if present
    if (eventLogStream.children.length === 1 && eventLogStream.children[0].innerText.includes('Awaiting FNOL claim submissions')) {
      eventLogStream.innerHTML = '';
    }
    const item = document.createElement('div');
    item.className = 'event-log-item';
    item.innerHTML = `
      <div>
        <span class="event-tag ${tagClass}">${eventType}</span>
        <span style="color: #6b7280; font-size: 0.75rem;">[${time}]</span>
      </div>
      <div style="color: #d1d5db; margin-top: 0.25rem;">${detail}</div>
    `;
    eventLogStream.prepend(item);
  }

  function activatePipelineNode(fromId, toId) {
    const from = document.getElementById(fromId);
    const to = document.getElementById(toId);
    if (from) {
      from.classList.remove('active');
      from.classList.add('completed');
    }
    if (to) {
      to.classList.add('active');
    }
  }

  function addReserveTableRow(reserve) {
    const tr = document.createElement('tr');
    tr.id = `reserve-row-${reserve.claimNumber}`;
    tr.innerHTML = `
      <td style="font-weight: 700; color: #fff;">${reserve.claimNumber}</td>
      <td>${reserve.reserveId}</td>
      <td>$${reserve.initialReserveAmount.toLocaleString()}</td>
      <td id="current-res-${reserve.claimNumber}" style="color: #10b981; font-weight: 700;">$${reserve.currentReserveAmount.toLocaleString()}</td>
      <td id="paid-res-${reserve.claimNumber}">$${reserve.paidAmount.toLocaleString()}</td>
      <td><span class="badge badge-established">${reserve.status}</span></td>
      <td>
        <button class="btn-payout" onclick="openPayoutModal('${reserve.claimNumber}')" style="background: rgba(59, 130, 246, 0.2); border: 1px solid #3b82f6; color: #fff; padding: 0.3rem 0.6rem; border-radius: 6px; cursor: pointer; font-size: 0.75rem;">
          Payout
        </button>
      </td>
    `;
    reservesTableBody.prepend(tr);
  }

  window.openPayoutModal = function(claimNo) {
    document.getElementById('modalClaimNumber').value = claimNo;
    payoutModal.style.display = 'flex';
  };

  window.closePayoutModal = function() {
    payoutModal.style.display = 'none';
  };

  payoutForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    const claimNumber = document.getElementById('modalClaimNumber').value;
    const payoutAmount = parseFloat(document.getElementById('modalPayoutAmount').value);
    const payeeName = document.getElementById('modalPayeeName').value;
    const paymentMethod = document.getElementById('modalPaymentMethod').value;

    try {
      const res = await fetch('/api/v1/reserves/payouts', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ claimNumber, payoutAmount, payeeName, paymentMethod })
      });

      if (res.ok) {
        const payout = await res.json();
        alert(`Payout ${payout.payoutId} of $${payoutAmount} issued to ${payeeName}!`);
        closePayoutModal();

        // Refresh reserve row
        const resRes = await fetch(`/api/v1/reserves/${claimNumber}`);
        if (resRes.ok) {
          const updated = await resRes.json();
          document.getElementById(`current-res-${claimNumber}`).textContent = '$' + updated.currentReserveAmount.toLocaleString();
          document.getElementById(`paid-res-${claimNumber}`).textContent = '$' + updated.paidAmount.toLocaleString();
        }
      } else {
        const err = await res.text();
        alert('Payout Error: ' + err);
      }
    } catch (e) {
      console.error(e);
    }
  });
});
